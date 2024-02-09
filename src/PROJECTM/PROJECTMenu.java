package PROJECTM;

import java.util.Scanner;

public class PROJECTMenu {
    public static void main(String[] args) {
        // Obtén la instancia de ConnectionFactory
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

        // Menú de opciones
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n" + "1. Borrar tablas de la base de datos y su información");
            System.out.println("2. Crear tablas de la base de datos");
            System.out.println("3. Poblar masivamente las tablas desde el XML" + "\n");
            System.out.println("4. Seleccionar todos los elementos que contengan un texto concreto");
            System.out.println("5. Seleccionar todos los elementos que cumplan una condición");
            System.out.println("6. Seleccionar elementos concretos" + "\n");
            System.out.println("7. Seleccionar un elemento concreto y permitir su modificación");
            System.out.println("8. Modificar diferentes registros de información" + "\n");
            System.out.println("9. Eliminar un registro concreto de información");
            System.out.println("10. Eliminar un conjunto de registros de información que cumplan una condición");
            System.out.println("11. Salir" + "\n");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    PROJECTMain.borrarTablas(connectionFactory);
                    break;
                case 2:
                    PROJECTMain.crearTablas(connectionFactory);
                    break;
                case 3:
                    PROJECTMain.poblarMasivamente(connectionFactory);
                    break;


                case 4:
                    PROJECTMain.seleccionarConTexto(connectionFactory);
                    break;
                case 5:
                    PROJECTMain.seleccionarElementosPorCondicion(connectionFactory);
                    break;
                case 6:
                    PROJECTMain.seleccionarElementosConcretos(connectionFactory);
                    break;


                case 7:
                    PROJECTMain.modificarRegistro(connectionFactory);
                    break;
                case 8:
                    PROJECTMain.modificarRegistros(connectionFactory);
                    break;


                case 9:
                    PROJECTMain.eliminarRegistro(connectionFactory);
                    break;

                case 10:
                    PROJECTMain.eliminarRegistros(connectionFactory);
                    break;
            }

        } while (opcion != 11);
        scanner.close();

    }
}
