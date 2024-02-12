package main;

import IO.IO;
import crud.CRUD;

public class Main {
	public static void main(String[] args) {
		try {
			CRUD crud = new CRUD();
			mostrarMenu(crud); // Llama a un método para mostrar el menú
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
			IO.println("3. Eliminar Atributo a un Piloto");
			IO.println("4. Busqueda");
			IO.println("5. Mostrar Pilotos y Palmarés");
			IO.println("6. Simular Fin de Semana");
			IO.println("7. Clasificacion del Mundial");
			IO.println("8. Modificar Piloto");
			IO.println("9. Salir");
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
				crud.borrarAtributoPiloto();
				break;
			case 4:
				crud.buscarPorAtributoValor();
				break;
			case 5:
				crud.mostrarPilotos();
				break;
			case 6:
				crud.simularFindeSemana();
				break;
			case 7:
				crud.clasificacionPorPuntos();
				break;
			case 8:
				crud.modificarPiloto();
				break;
			case 9:
				IO.println("¡HASTA EL PROXIMO MUNDIAL!");
				System.exit(0);
			default:
				IO.println("Opción no válida. Inténtelo de nuevo.");
			}
		}
	}
}
