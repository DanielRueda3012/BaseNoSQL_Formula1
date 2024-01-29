package main;

import IO.IO;
import crud.CRUD;

public class Main {
    public static void main(String[] args) {
        try {
            CRUD crud = new CRUD();
            mostrarMenu(crud);  // Llama a un método para mostrar el menú
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Método para mostrar el menú
    private static void mostrarMenu(CRUD crud) {

        while (true) {
        	IO.println("\nMenú:");
            IO.println("1. Insertar Piloto");
            IO.println("2. Eliminar Piloto");
            IO.println("3. Busqueda");
            IO.println("4. Mostrar Pilotos y Palmarés");
            IO.println("5. Simular Fin de Semana");
            IO.println("6. Clasificacion del Mundial");
            IO.println("7. Modificar Piloto");
            IO.println("8. Salir");
            IO.println("Seleccione una opción: ");

            int opcion = IO.readInt();  

            switch (opcion) {
                case 1:
                    crud.insertarPiloto();
                    break;
                case 2:
                    crud.eliminaPiloto();
                    break;
                case 3:
                	crud.buscarPorAtributoValor();
                    break;
                case 4:
                    crud.mostrarPilotos();
                    break;
                case 5:
                	crud.simularFindeSemana();
                    break;
                case 6:
                	crud.clasificacionPorPuntos();
                    break;
                case 7:
                    crud.modificarPiloto();
                    break;
                case 8:
                	 IO.println("¡HASTA EL PROXIMO MUNDIAL!");
                    System.exit(0);  
                default:
                	 IO.println("Opción no válida. Inténtelo de nuevo.");
            }
        }
    }
}
