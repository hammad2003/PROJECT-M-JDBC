package PROJECTM;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PROJECTMain {

    public static void borrarTablas(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS Juego, Mod, Detalle, Categoria;");
            System.out.println("\n" + "Tablas borradas exitosamente." + "\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    public static void crearTablas(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Sentencia SQL para crear la tabla Juego
            String crearTablaJuego = "CREATE TABLE Juego (" +
                    "JuegoID SERIAL PRIMARY KEY," +
                    "Nombre VARCHAR(255) NOT NULL," +
                    "Descripcion TEXT);";

            // Sentencia SQL para crear la tabla Mod
            String crearTablaMod = "CREATE TABLE Mod (" +
                    "ModID SERIAL PRIMARY KEY," +
                    "JuegoID INT," +
                    "Nombre VARCHAR(255) NOT NULL," +
                    "Autor VARCHAR(255) NOT NULL," +
                    "Descripcion TEXT," +
                    "FOREIGN KEY (JuegoID) REFERENCES Juego(JuegoID)" +
                    ");";

            // Sentencia SQL para crear la tabla Detalles
            String crearTablaDetalle = "CREATE TABLE Detalle (" +
                    "DetalleID SERIAL PRIMARY KEY," +
                    "ModID INT," +
                    "Descripcion TEXT," +
                    "FOREIGN KEY (ModID) REFERENCES Mod(ModID)" +
                    ");";

            // Sentencia SQL para crear la tabla Categoria
            String crearTablaCategoria = "CREATE TABLE Categoria (" +
                    "CategoriaID SERIAL PRIMARY KEY," +
                    "ModID INT," +
                    "Nombre VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY (ModID) REFERENCES Mod(ModID));";

            // Ejecutar las sentencias SQL
            statement.executeUpdate(crearTablaJuego);
            statement.executeUpdate(crearTablaMod);
            statement.executeUpdate(crearTablaDetalle);
            statement.executeUpdate(crearTablaCategoria);

            System.out.println("\n" + "Tablas creadas exitosamente." + "\n");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    public static void poblarMasivamente(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.connect()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new FileReader("CurseForge.xml"));
            Document document = dBuilder.parse(inputSource);

            document.getDocumentElement().normalize();

            NodeList juegosNodeList = document.getElementsByTagName("Juego");

            for (int temp = 0; temp < juegosNodeList.getLength(); temp++) {
                Node juegoNode = juegosNodeList.item(temp);

                if (juegoNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element juegoElement = (Element) juegoNode;

                    String nombreJuego = juegoElement.getElementsByTagName("Nombre").item(0).getTextContent();
                    String descripcionJuego = juegoElement.getElementsByTagName("Descripcion").item(0).getTextContent();

                    try (PreparedStatement insertJuego = connection.prepareStatement("INSERT INTO Juego (Nombre, Descripcion) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                        insertJuego.setString(1, nombreJuego);
                        insertJuego.setString(2, descripcionJuego);
                        insertJuego.executeUpdate();

                        try (ResultSet juegoResultSet = insertJuego.getGeneratedKeys()) {
                            if (juegoResultSet.next()) {
                                int juegoID = juegoResultSet.getInt(1);

                                NodeList modsNodeList = juegoElement.getElementsByTagName("Mod");
                                for (int modIndex = 0; modIndex < modsNodeList.getLength(); modIndex++) {
                                    Node modNode = modsNodeList.item(modIndex);

                                    if (modNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element modElement = (Element) modNode;

                                        String nombreMod = modElement.getElementsByTagName("Nombre").item(0).getTextContent();
                                        String autorMod = modElement.getElementsByTagName("Autor").item(0).getTextContent();
                                        String descripcionMod = modElement.getElementsByTagName("Descripcion").item(0).getTextContent();

                                        // Insertar en la tabla Mod
                                        try (PreparedStatement insertMod = connection.prepareStatement("INSERT INTO Mod (JuegoID, Nombre, Autor, Descripcion) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                                            insertMod.setInt(1, juegoID);
                                            insertMod.setString(2, nombreMod);
                                            insertMod.setString(3, autorMod);
                                            insertMod.setString(4, descripcionMod);
                                            insertMod.executeUpdate();

                                            try (ResultSet modResultSet = insertMod.getGeneratedKeys()) {
                                                if (modResultSet.next()) {
                                                    int modID = modResultSet.getInt(1);

                                                    // Insertar en la tabla Detalle (si hay detalles)
                                                    NodeList detallesNodeList = modElement.getElementsByTagName("Detalles");
                                                    if (detallesNodeList.getLength() > 0) {
                                                        Node detallesNode = detallesNodeList.item(0);
                                                        if (detallesNode.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element detallesElement = (Element) detallesNode;
                                                            NodeList detalleNodes = detallesElement.getElementsByTagName("Detalle");

                                                            for (int detalleIndex = 0; detalleIndex < detalleNodes.getLength(); detalleIndex++) {
                                                                Node detalleNode = detalleNodes.item(detalleIndex);
                                                                if (detalleNode.getNodeType() == Node.ELEMENT_NODE) {
                                                                    Element detalleElement = (Element) detalleNode;
                                                                    String descripcionDetalle = detalleElement.getElementsByTagName("Descripcion").item(0).getTextContent();

                                                                    // Insertar en la tabla Detalle
                                                                    try (PreparedStatement insertDetalle = connection.prepareStatement("INSERT INTO Detalle (ModID, Descripcion) VALUES (?, ?)")) {
                                                                        insertDetalle.setInt(1, modID);
                                                                        insertDetalle.setString(2, descripcionDetalle);
                                                                        insertDetalle.executeUpdate();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    // Insertar categorías (si las hay)
                                                    NodeList categoriasNodeList = modElement.getElementsByTagName("Categoria");
                                                    for (int categoriaIndex = 0; categoriaIndex < categoriasNodeList.getLength(); categoriaIndex++) {
                                                        Node categoriaNode = categoriasNodeList.item(categoriaIndex);

                                                        if (categoriaNode.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element categoriaElement = (Element) categoriaNode;
                                                            String nombreCategoria = categoriaElement.getElementsByTagName("Nombre").item(0).getTextContent();

                                                            // Insertar en la tabla Categoria
                                                            try (PreparedStatement insertCategoria = connection.prepareStatement("INSERT INTO Categoria (ModID, Nombre) VALUES (?, ?)")) {
                                                                insertCategoria.setInt(1, modID);
                                                                insertCategoria.setString(2, nombreCategoria);
                                                                insertCategoria.executeUpdate();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("\n" + "Tablas pobladas masivamente con exito." + "\n");
            connectionFactory.disconnect();
        }
    }



    public static void printTablas() {
        System.out.println("Tablas disponibles:");
        System.out.println("1. Juego");
        System.out.println("2. Mod");
        System.out.println("3. Detalle");
        System.out.println("4. Categoria");
    }


    public static void seleccionarConTexto(ConnectionFactory connectionFactory) {
        Scanner scanner = new Scanner(System.in);

        // Imprimir las tablas disponibles y sus atributos
        printTablas();

        // Pedir al usuario que ingrese el número correspondiente a la tabla
        System.out.println("Ingrese el número correspondiente a la tabla en la que desea buscar el texto:");
        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        // Obtener el nombre de la tabla seleccionada y sus atributos
        String tablaSeleccionada;
        String columnaTexto;
        switch (opcionTabla) {
            case 1:
                tablaSeleccionada = "Juego";
                columnaTexto = "Nombre"; // Selecciona la columna donde buscar el texto
                System.out.println("La tabla Juego tiene los siguientes atributos: JuegoID, Nombre, Descripcion");
                break;
            case 2:
                tablaSeleccionada = "Mod";
                columnaTexto = "Nombre"; // Selecciona la columna donde buscar el texto
                System.out.println("La tabla Mod tiene los siguientes atributos: ModID, JuegoID, Nombre, Autor, Descripcion");
                break;
            case 3:
                tablaSeleccionada = "Detalle";
                columnaTexto = "Descripcion"; // Selecciona la columna donde buscar el texto
                System.out.println("La tabla Detalle tiene los siguientes atributos: DetalleID, ModID, Descripcion");
                break;
            case 4:
                tablaSeleccionada = "Categoria";
                columnaTexto = "Nombre"; // Selecciona la columna donde buscar el texto
                System.out.println("La tabla Categoria tiene los siguientes atributos: CategoriaID, ModID, Nombre");
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
                return; // Salir del método si la opción no es válida
        }

        // Pedir al usuario que ingrese el texto a buscar
        System.out.println("Ingrese el texto que desea buscar:");
        String texto = scanner.nextLine().trim();

        // Resto del código para seleccionar registros que contengan el texto ingresado
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Consulta para seleccionar todos los elementos que contengan el texto concreto
            String query = "SELECT * FROM " + tablaSeleccionada + " WHERE " + columnaTexto + " LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + texto + "%"); // El símbolo % se usa para buscar el texto en cualquier parte del campo
            ResultSet resultSet = preparedStatement.executeQuery();

            // Imprimir los resultados obtenidos
            System.out.println("Resultados de la búsqueda:" + "\n");
            while (resultSet.next()) {
                // Imprimir los datos de cada fila
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.println(resultSet.getMetaData().getColumnName(i) + ": " + resultSet.getString(i));
                }
                System.out.println(); // Agregar una línea en blanco entre cada registro
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    public static void seleccionarElementosPorCondicion(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Imprimir las tablas disponibles y sus atributos
            printTablas();

            // Pedir al usuario que ingrese el número correspondiente a la tabla
            Scanner scanner = new Scanner(System.in);
            System.out.println("Ingrese el número correspondiente a la tabla en la que desea seleccionar los elementos:");
            int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

            // Obtener el nombre de la tabla seleccionada y sus atributos
            String tablaSeleccionada;
            switch (opcionTabla) {
                case 1:
                    tablaSeleccionada = "Juego";
                    System.out.println("La tabla Juego tiene los siguientes atributos: JuegoID, Nombre, Descripcion");
                    break;
                case 2:
                    tablaSeleccionada = "Mod";
                    System.out.println("La tabla Mod tiene los siguientes atributos: ModID, JuegoID, Nombre, Autor, Descripcion");
                    break;
                case 3:
                    tablaSeleccionada = "Detalle";
                    System.out.println("La tabla Detalle tiene los siguientes atributos: DetalleID, ModID, Descripcion");
                    break;
                case 4:
                    tablaSeleccionada = "Categoria";
                    System.out.println("La tabla Categoria tiene los siguientes atributos: CategoriaID, ModID, Nombre");
                    break;
                default:
                    System.out.println("La opción ingresada no es válida.");
                    return; // Salir del método si la opción no es válida
            }

            // Pedir al usuario que ingrese la condición
            System.out.println("Ingrese la condición para seleccionar los elementos (Ejemplo: Nombre = 'Valor'):");
            String condicion = scanner.nextLine().trim();

            // Consulta para seleccionar los elementos que cumplan la condición
            String query = "SELECT * FROM " + tablaSeleccionada + " WHERE " + condicion;
            ResultSet resultSet = statement.executeQuery(query);

            // Mostrar los resultados de la consulta
            System.out.println("Resultados de la consulta:" + "\n");
            while (resultSet.next()) {
                // Imprimir cada registro encontrado
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.println(resultSet.getMetaData().getColumnName(i) + ": " + resultSet.getString(i));
                }
                System.out.println(); // Salto de línea entre registros
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    public static void seleccionarElementosConcretos(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Imprimir las tablas disponibles y sus atributos
            printTablas();

            // Pedir al usuario que ingrese el número correspondiente a la tabla
            Scanner scanner = new Scanner(System.in);
            System.out.println("Ingrese el número correspondiente a la tabla en la que desea seleccionar los elementos:");
            int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

            // Obtener el nombre de la tabla seleccionada y sus atributos
            String tablaSeleccionada;
            switch (opcionTabla) {
                case 1:
                    tablaSeleccionada = "Juego";
                    System.out.println("La tabla Juego tiene los siguientes atributos: JuegoID, Nombre, Descripcion");
                    break;
                case 2:
                    tablaSeleccionada = "Mod";
                    System.out.println("La tabla Mod tiene los siguientes atributos: ModID, JuegoID, Nombre, Autor, Descripcion");
                    break;
                case 3:
                    tablaSeleccionada = "Detalle";
                    System.out.println("La tabla Detalle tiene los siguientes atributos: DetalleID, ModID, Descripcion");
                    break;
                case 4:
                    tablaSeleccionada = "Categoria";
                    System.out.println("La tabla Categoria tiene los siguientes atributos: CategoriaID, ModID, Nombre");
                    break;
                default:
                    System.out.println("La opción ingresada no es válida.");
                    return; // Salir del método si la opción no es válida
            }

            // Pedir al usuario que ingrese el ID del registro a seleccionar
            System.out.println("Ingrese el ID del registro que desea seleccionar:");
            int idRegistro = Integer.parseInt(scanner.nextLine().trim());

            // Consulta para seleccionar el registro específico
            String query = "SELECT * FROM " + tablaSeleccionada + " WHERE " + tablaSeleccionada + "ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idRegistro);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Mostrar los resultados de la consulta
            System.out.println("Resultado de la consulta:" + "\n");
            if (resultSet.next()) {
                // Imprimir el registro encontrado
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.println(resultSet.getMetaData().getColumnName(i) + ": " + resultSet.getString(i));
                }
            } else {
                System.out.println("El registro con ID " + idRegistro + " no existe en la tabla " + tablaSeleccionada + ".");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }




    public static void modificarRegistro(ConnectionFactory connectionFactory) {
        Scanner scanner = new Scanner(System.in);

        // Imprimir las tablas disponibles
        printTablas();

        // Pedir al usuario que ingrese el número correspondiente a la tabla
        System.out.println("Ingrese el número correspondiente a la tabla en la que desea modificar el registro:");
        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        // Switch para seleccionar la tabla y mostrar sus atributos
        String tabla;
        switch (opcionTabla) {
            case 1:
                tabla = "Juego";
                System.out.println("La tabla Juego tiene los siguientes atributos: JuegoID, Nombre, Descripcion");
                break;
            case 2:
                tabla = "Mod";
                System.out.println("La tabla Mod tiene los siguientes atributos: ModID, JuegoID, Nombre, Autor, Descripcion");
                break;
            case 3:
                tabla = "Detalle";
                System.out.println("La tabla Detalle tiene los siguientes atributos: DetalleID, ModID, Descripcion");
                break;
            case 4:
                tabla = "Categoria";
                System.out.println("La tabla Categoria tiene los siguientes atributos: CategoriaID, ModID, Nombre");
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
                return; // Salir del método si la opción no es válida
        }

        // Pedir al usuario que ingrese el ID del registro a modificar
        System.out.println("Ingrese el ID del registro que desea modificar:");
        int idRegistro = Integer.parseInt(scanner.nextLine().trim());

        // Resto del código para modificar el registro según el ID ingresado
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Consulta para seleccionar el registro específico
            String query = "SELECT * FROM " + tabla + " WHERE " + tabla + "ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idRegistro);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Verificar si se encontró el registro
            if (resultSet.next()) {
                // Mostrar la información actual del registro
                System.out.println("Información actual del registro:");
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.println(resultSet.getMetaData().getColumnName(i) + ": " + resultSet.getString(i));
                }

                // Pedir al usuario que ingrese los nuevos valores
                System.out.println("\nIngrese los nuevos valores (presione Enter para mantener el valor actual):");
                System.out.println("\nTODOS LOS REGISTROS DE LOS ATRIBUTOS ID's NO SE RECOMIENDAN MODIFICAR: \n");
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.print(resultSet.getMetaData().getColumnName(i) + ": ");
                    String nuevoValor = scanner.nextLine().trim();
                    // Si el usuario ingresó un nuevo valor, actualizarlo en la base de datos
                    if (!nuevoValor.isEmpty()) {
                        String nombreColumna = resultSet.getMetaData().getColumnName(i);
                        String updateQuery = "UPDATE " + tabla + " SET " + nombreColumna + " = ? WHERE " + tabla + "ID = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setString(1, nuevoValor);
                        updateStatement.setInt(2, idRegistro);
                        updateStatement.executeUpdate();
                    }
                }
                System.out.println("\nRegistro modificado exitosamente.");
            } else {
                System.out.println("El registro con ID " + idRegistro + " no existe en la tabla " + tabla + ".");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    public static void modificarRegistros(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Imprimir las tablas disponibles y sus atributos
            printTablas();

            // Pedir al usuario que ingrese el número correspondiente a la tabla
            Scanner scanner = new Scanner(System.in);
            System.out.println("Ingrese el número correspondiente a la tabla en la que desea modificar el registro:");
            int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

            // Obtener el nombre de la tabla seleccionada y sus atributos
            String tablaSeleccionada;
            String[] atributos;
            switch (opcionTabla) {
                case 1:
                    tablaSeleccionada = "Juego";
                    atributos = new String[]{"JuegoID", "Nombre", "Descripcion"};
                    break;
                case 2:
                    tablaSeleccionada = "Mod";
                    atributos = new String[]{"ModID", "JuegoID", "Nombre", "Autor", "Descripcion"};
                    break;
                case 3:
                    tablaSeleccionada = "Detalle";
                    atributos = new String[]{"DetalleID", "ModID", "Descripcion"};
                    break;
                case 4:
                    tablaSeleccionada = "Categoria";
                    atributos = new String[]{"CategoriaID", "ModID", "Nombre"};
                    break;
                default:
                    System.out.println("La opción ingresada no es válida.");
                    return; // Salir del método si la opción no es válida
            }

            // Imprimir los atributos de la tabla seleccionada
            System.out.println("La tabla " + tablaSeleccionada + " tiene los siguientes atributos:");
            for (String atributo : atributos) {
                System.out.println(atributo);
            }

            // Pedir al usuario que ingrese el nombre del atributo que desea modificar
            System.out.println("Ingrese el nombre del atributo que desea modificar:");
            String atributoModificar = scanner.nextLine().trim();

            // Pedir al usuario que ingrese el nuevo valor para el atributo
            System.out.println("Ingrese el nuevo valor para el atributo:");
            String nuevoValor = scanner.nextLine().trim();

            // Pedir al usuario que ingrese el valor actual del atributo para filtrar los registros
            System.out.println("Ingrese el valor actual del atributo para filtrar los registros:");
            String valor = scanner.nextLine().trim();

            // Resto del código para modificar registros...
            // Aquí puedes implementar la lógica para modificar los registros de la tabla seleccionada
            String query = "UPDATE " + tablaSeleccionada + " SET " + atributoModificar + " = ? WHERE " + atributoModificar + " = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nuevoValor);
            preparedStatement.setString(2, valor);
            int rowCount = preparedStatement.executeUpdate();

            if (rowCount > 0) {
                System.out.println("\n" + "Registros modificados exitosamente.");
            } else {
                System.out.println("\n" + "No se encontraron registros que coincidan con los criterios proporcionados.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }




    public static void eliminarRegistro(ConnectionFactory connectionFactory) {
        Scanner scanner = new Scanner(System.in);

        // Imprimir las tablas disponibles
        printTablas();

        // Pedir al usuario que ingrese el número correspondiente a la tabla
        System.out.println("Ingrese el número correspondiente a la tabla en la que desea eliminar el registro:");
        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        // Switch para seleccionar la tabla y mostrar sus atributos
        String tabla;
        switch (opcionTabla) {
            case 1:
                tabla = "Juego";
                System.out.println("La tabla Juego tiene los siguientes atributos: JuegoID, Nombre, Descripcion");
                break;
            case 2:
                tabla = "Mod";
                System.out.println("La tabla Mod tiene los siguientes atributos: ModID, JuegoID, Nombre, Autor, Descripcion");
                break;
            case 3:
                tabla = "Detalle";
                System.out.println("La tabla Detalle tiene los siguientes atributos: DetalleID, ModID, Descripcion");
                break;
            case 4:
                tabla = "Categoria";
                System.out.println("La tabla Categoria tiene los siguientes atributos: CategoriaID, ModID, Nombre");
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
                return; // Salir del método si la opción no es válida
        }

        // Pedir al usuario que ingrese el ID del registro a eliminar
        System.out.println("Ingrese el ID del registro que desea eliminar:");
        int idRegistro = Integer.parseInt(scanner.nextLine().trim());

        // Resto del código para eliminar el registro según el ID ingresado
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Consulta para eliminar el registro específico
            String query = "DELETE FROM " + tabla + " WHERE " + tabla + "ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idRegistro);
            int rowsAffected = preparedStatement.executeUpdate();

            // Verificar si se eliminó el registro
            if (rowsAffected > 0) {
                System.out.println("\n" + "Registro eliminado exitosamente.");
            } else {
                System.out.println("\n" + "El registro con ID " + idRegistro + " no existe en la tabla " + tabla + ".");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    public static void eliminarRegistros(ConnectionFactory connectionFactory) {
        Scanner scanner = new Scanner(System.in);

        // Imprimir las tablas disponibles y sus atributos
        printTablas();

        // Pedir al usuario que ingrese el número correspondiente a la tabla
        System.out.println("Ingrese el número correspondiente a la tabla en la que desea eliminar el registro:");
        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        // Obtener el nombre de la tabla seleccionada y sus atributos
        String tablaSeleccionada;
        switch (opcionTabla) {
            case 1:
                tablaSeleccionada = "Juego";
                System.out.println("La tabla Juego tiene los siguientes atributos: JuegoID, Nombre, Descripcion");
                break;
            case 2:
                tablaSeleccionada = "Mod";
                System.out.println("La tabla Mod tiene los siguientes atributos: ModID, JuegoID, Nombre, Autor, Descripcion");
                break;
            case 3:
                tablaSeleccionada = "Detalle";
                System.out.println("La tabla Detalle tiene los siguientes atributos: DetalleID, ModID, Descripcion");
                break;
            case 4:
                tablaSeleccionada = "Categoria";
                System.out.println("La tabla Categoria tiene los siguientes atributos: CategoriaID, ModID, Nombre");
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
                return; // Salir del método si la opción no es válida
        }

        // Pedir al usuario que ingrese la condición para eliminar registros
        System.out.println("Ingrese la condición para eliminar los registros (por ejemplo: Nombre = 'Juego1'):");
        String condicion = scanner.nextLine().trim();

        // Resto del código para eliminar los registros según la tabla y la condición ingresadas
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Consulta para eliminar los registros que cumplan la condición proporcionada
            String query = "DELETE FROM " + tablaSeleccionada + " WHERE " + condicion;
            int rowsAffected = statement.executeUpdate(query);

            // Verificar si se eliminaron registros
            if (rowsAffected > 0) {
                System.out.println("\n" + "Se eliminaron " + rowsAffected + " registros de la tabla " + tablaSeleccionada + ".");
            } else {
                System.out.println("\n" + "No se encontraron registros que cumplan la condición proporcionada en la tabla " + tablaSeleccionada + ".");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


}