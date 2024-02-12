package crud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import IO.IO;
import conexion.Conexion;
import model.Driver;

import static com.mongodb.client.model.Filters.eq;

public class CRUD {
    Conexion c = new Conexion();
    MongoCollection<Driver> collPilotos;
    ArrayList<Document> arrayPilotos;
    ArrayList<Document> arrayCarrera;

    // CONSTRUCTOR
    public CRUD() {
        collPilotos = c.getDriversCollection();
    }

    public boolean insertarPiloto() {
    	String nuevoValor = null,nuevoAtributo = null;
    	
    	
        IO.println("Ingrese el nombre del nuevo piloto:");
        String nombre = IO.readString();

        IO.println("Ingrese el equipo del nuevo piloto:");
        String equipo = IO.readString();

        IO.println("Ingrese la seguridad del nuevo piloto:");
        int seguridad = IO.readInt();

        // Preguntar al usuario si desea ingresar victorias
        IO.println("¿Desea ingresar el número de victorias? (s/n)");
        char opcionVictorias = IO.readChar();
        int victoria = (opcionVictorias == 's') ? IO.readInt() : 0;

        // Preguntar al usuario si desea ingresar vueltas rápidas
        IO.println("¿Desea ingresar el número de vueltas rápidas? (s/n)");
        char opcionVueltasRapidas = IO.readChar();
        int vueltaRapida = (opcionVueltasRapidas == 's') ? IO.readInt() : 0;

        // Preguntar al usuario si desea ingresar el palmarés
        IO.println("¿Desea ingresar el palmarés? (s/n)");
        char opcionPalmares = IO.readChar();
        List<String> listaPalmares = (opcionPalmares == 's') ? ingresarPalmares() : new ArrayList<>();
        
        // Crear documento con los nuevos atributos
        Document nuevoPiloto = new Driver()
            .append("nombre", nombre)
            .append("equipo", equipo)
            .append("seguridad", seguridad)
            .append("victorias", victoria)
            .append("vueltaRapidas", vueltaRapida)
            .append("palmares", listaPalmares);
        	
        
        
        do {
            IO.println("¿Desea ingresar un atributo-valor nuevo? (s/n)");
            char opcionNuevoAtributoValor = IO.readChar();

            // Verificar si el usuario desea ingresar un nuevo atributo-valor
            if (opcionNuevoAtributoValor == 's') {
                IO.println("Ingrese el nombre del nuevo atributo:");
                nuevoAtributo = IO.readString();

                IO.println("Ingrese el valor del nuevo atributo:");
                nuevoValor = IO.readString();


                IO.println("Nuevo atributo ingresado: " + nuevoAtributo + " con valor: " + nuevoValor);
                nuevoPiloto.append(nuevoAtributo, nuevoValor);
            } else {
                break;
            }
        } while (true);
        
        	            
        // Insertar el nuevo piloto en la base de datos
        collPilotos.insertOne((Driver) nuevoPiloto);

        IO.println("Piloto insertado: " + nombre);
        return true;
    }

    private List<String> ingresarPalmares() {
        IO.println("Ingrese el palmarés del piloto (separado por comas):");
        String palmaresInput = IO.readString();
        return Arrays.asList(palmaresInput.split(","));
    }
    public boolean eliminaPiloto() {
        IO.println("Ingrese el nombre a eliminar:");
        String nombre = IO.readString();

        // Eliminar el piloto de la base de datos
        collPilotos.deleteOne(eq("nombre", nombre));
        IO.println("Piloto eliminado: " + nombre);

        return true;
    }
    public void borrarAtributoPiloto() {
        // Solicitar al usuario el nombre del piloto
        IO.println("Ingrese el nombre del piloto:");
        String nombrePiloto = IO.readString();

        // Buscar el piloto por nombre
        Document filtro = new Document("nombre", nombrePiloto);
        Document piloto = collPilotos.find(filtro).first();

        if (piloto != null) {
            // Solicitar al usuario el nombre del atributo a borrar
            IO.println("Ingrese el nombre del atributo que desea borrar:");
            String atributo = IO.readString();

            // Verificar si el atributo existe en el piloto
            if (piloto.containsKey(atributo)) {
                // Crear un documento de actualización para eliminar el atributo
                Document updateDocument = new Document("$unset", new Document(atributo, ""));

                // Aplicar la actualización al documento que coincide con el filtro
                collPilotos.updateOne(filtro, updateDocument);

                // Mensaje indicando que el atributo ha sido eliminado
                IO.println("Atributo '" + atributo + "' eliminado del piloto '" + nombrePiloto + "'.");
            } else {
                IO.println("El atributo '" + atributo + "' no existe en el piloto '" + nombrePiloto + "'.");
            }
        } else {
            IO.println("Piloto no encontrado.");
        }
    }


    public void buscarPorAtributoValor() {
        IO.println("Ingrese el nombre del atributo por el cual desea buscar:");
        IO.println(" Equipo");
        IO.println(" Nombre");
        IO.println(" Seguridad");
        IO.println(" Victorias");
        IO.println(" VueltaRápidas");
        IO.println(" Palmarés");
        IO.println(" Otros");
        String atributoBusqueda = IO.readString();

        IO.println("Ingrese el valor del atributo:");
        String valorBusqueda = IO.readString();

        // Construir el filtro para la búsqueda
        Document filtro = new Document(atributoBusqueda, valorBusqueda);

        // Realizar la búsqueda en la base de datos
        ArrayList<Driver> pilotosEncontrados = new ArrayList<>();
        collPilotos.find(filtro).into(pilotosEncontrados);

        if (!pilotosEncontrados.isEmpty()) {
            IO.println("Resultados de la búsqueda:");
            for (Driver pilotoEncontrado : pilotosEncontrados) {
                String pilotoString = pilotoEncontrado.toString();
                // Buscar la posición de la primera coma
                int primeraComa = pilotoString.indexOf(',');

                if (primeraComa != -1) {
                    String despuesPrimeraComa = pilotoString.substring(primeraComa + 1).trim();
                    IO.println(despuesPrimeraComa);
                } else {
                    IO.println(pilotoString);
                }
            }
        } else {
            IO.println("No se encontraron resultados para la búsqueda.");
        }
    }

    public void mostrarPilotos() {
        arrayPilotos = new ArrayList<>();
        collPilotos.find().into(arrayPilotos);

        for (Document d : arrayPilotos) {
            String pilotoString = d.toString();
            // Buscar la posición de la primera coma
            int primeraComa = pilotoString.indexOf(',');

            if (primeraComa != -1) {
                // Mostrar la cadena después de la primera coma
                String despuesPrimeraComa = pilotoString.substring(primeraComa + 1).trim();
                IO.println(despuesPrimeraComa);
            } else {
                // En caso de que no haya coma, mostrar el documento completo
                IO.println(pilotoString);
            }
        }
    }

    public boolean pilotoGanador() {
        arrayCarrera = new ArrayList<>();

        String ganador = "Nadie";
        int niv = 0;

        IO.println("Elige dos pilotos para que corran");
        String p1;
        String p2;

        IO.println("Piloto 1?");
        p1 = IO.readString();

        IO.println("Piloto 2?");
        p2 = IO.readString();

        collPilotos.find(eq("nombre", p1)).into(arrayCarrera);
        collPilotos.find(eq("nombre", p2)).into(arrayCarrera);

        for (Document d : arrayCarrera) {
            if (d.getInteger("nivel") > niv) {
                ganador = d.getString("nombre");
                niv = d.getInteger("nivel");
            }
        }

        IO.println("El ganador es " + ganador);
        return true;
    }

    public void simularFindeSemana() {
        Random random = new Random();
        arrayPilotos = new ArrayList<>();
        collPilotos.find().into(arrayPilotos);

        // Mezclar la lista de pilotos de manera aleatoria
        Collections.shuffle(arrayPilotos);

        // Realizar clasificatoria
        List<Document> clasificados = new ArrayList<>(arrayPilotos);

        // Imprimir clasificación de la clasificatoria
        IO.println("\nResultado de la clasificación:");
        for (int i = 0; i < clasificados.size(); i++) {
            IO.println((i + 1) + ". " + clasificados.get(i).getString("nombre"));
        }

        // Asignar puntuaciones a los pilotos en la carrera
        int puntos = 10;
        List<String> ordenLlegada = new ArrayList<>();
        List<String> abandonados = new ArrayList<>();

        for (Document piloto : arrayPilotos.subList(0, Math.min(10, arrayPilotos.size()))) {
            double probabilidadAbandono = random.nextDouble() * piloto.getInteger("seguridad");
            if (probabilidadAbandono < 1) {
                abandonados.add(piloto.getString("nombre"));
                IO.println(piloto.getString("nombre") + " ha tenido un accidente y abandona la carrera.");
            } else {
                ordenLlegada.add(piloto.getString("nombre"));
                IO.println(piloto.getString("nombre") + " completa la carrera y recibe " + puntos + " puntos.");
                // Actualizar puntuación en la base de datos
                collPilotos.updateOne(eq("nombre", piloto.getString("nombre")),
                        new Document("$inc", new Document("puntos", puntos)));
                puntos--;
            }
        }

        // Imprimir orden de llegada
        IO.println("\nResultados del GP:");
        for (int i = 0; i < ordenLlegada.size(); i++) {
            IO.println((i + 1) + ". " + ordenLlegada.get(i));
        }

        // Imprimir pilotos que abandonaron
        IO.println("\nPilotos que abandonaron:");
        for (String abandonado : abandonados) {
            IO.println(abandonado);
        }
    }
    public void clasificacionPorPuntos() {
        arrayPilotos = new ArrayList<>();
        collPilotos.find().projection(new Document("nombre", 1).append("puntos", 1))
                         .sort(new Document("puntos", -1)).into(arrayPilotos);

        IO.println("\nClasificación General por Puntos:");
        for (int i = 0; i < arrayPilotos.size(); i++) {
            Document piloto = arrayPilotos.get(i);
            String nombre = piloto.getString("nombre");
            int puntos = piloto.getInteger("puntos", 0);  // Se establece 0 puntos si el campo no está presente
            IO.println((i + 1) + ". " + nombre + " - Puntos: " + puntos);
        }
    }


    public boolean modificarPiloto() {
        IO.println("Ingrese el nombre del piloto que desea modificar:");
        String nombre = IO.readString();

        // Buscar el piloto por nombre
        Document piloto = buscarPilotoPorNombre(nombre);
        if (piloto == null) {
            IO.println("Piloto no encontrado.");
            return false;
        }

        IO.println("¿Qué desea modificar?");
        IO.println("1. Equipo");
        IO.println("2. Nombre");
        IO.println("3. Seguridad");
        IO.println("4. Victoria");
        IO.println("5. Vuelta Rápida");
        IO.println("6. Palmarés");
        IO.println("7. Otro");
        IO.println("8. Agregar nuevo atributo y valor");

        int opcion = IO.readInt();
        IO.readString();
        switch (opcion) {
            case 1:
                modificarAtributo(piloto, "equipo", IO.readString());
                break;
            case 2:
                modificarAtributo(piloto, "nombre", IO.readString());
                break;
            case 3:
                modificarAtributo(piloto, "seguridad", IO.readInt());
                break;
            case 4:
                modificarAtributo(piloto, "victoria", IO.readInt());
                break;
            case 5:
                modificarAtributo(piloto, "vueltaRapida", IO.readInt());
                break;
            case 6:
                IO.println("Ingrese el nuevo palmarés (separado por comas):");
                IO.readString();
                String nuevoPalmares = IO.readString();
                modificarAtributo(piloto, "palmares", Arrays.asList(nuevoPalmares.split(",")));
                break;
            case 7:
            	IO.println("Ingrese el atributo que quieras cambiar:");
            	IO.readString();
            	String atributoCambio=IO.readString();
                IO.println("Ingrese el nuevo valor:");
                IO.readString();
                String nuevoValorAtributo = IO.readString();
                modificarAtributo(piloto, atributoCambio,nuevoValorAtributo);
                break;
            case 8:
                IO.println("Ingrese el nombre del nuevo atributo:");
                String nuevoAtributo = IO.readString();
                IO.println("Ingrese el valor del nuevo atributo:");
                String nuevoValor = IO.readString();
                modificarAtributo(piloto, nuevoAtributo, nuevoValor);
                break;
            default:
                IO.println("Opción no válida.");
                return false;
        }

        // Actualizar el piloto en la base de datos
        collPilotos.replaceOne(Filters.eq("nombre", nombre), (Driver) piloto);
        IO.println("Piloto modificado exitosamente.");
        return true;
    }

    private void modificarAtributo(Document piloto, String atributo, Object valor) {
        piloto.put(atributo, valor);
    }


    // Método auxiliar para buscar un piloto por nombre
    private Document buscarPilotoPorNombre(String nombre) {
        List<Document> pilotos = new ArrayList<>();
        collPilotos.find(Filters.eq("nombre", nombre)).into(pilotos);

        if (!pilotos.isEmpty()) {
            return pilotos.get(0);
        }

        return null;
    }
    
    
}