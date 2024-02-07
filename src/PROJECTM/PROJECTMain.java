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

    public static void main(String[] args) {
        // Obtén la instancia de ConnectionFactory
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

        // Menú de opciones
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("1. Borrar tablas de la base de datos y su información");
            System.out.println("2. Crear tablas de la base de datos");
            System.out.println("3. Poblar masivamente las tablas desde el XML");
            System.out.println("4. Seleccionar todos los elementos que contengan un texto concreto");
            System.out.println("5. Seleccionar un elemento concreto y permitir su modificación");
            System.out.println("6. Posibilidad de modificar diferentes registros de información");
            System.out.println("7. Posibilidad de eliminar un registro concreto de información");
            System.out.println("8. Posibilidad de eliminar un conjunto de registros de información que cumplan una condición");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    borrarTablas(connectionFactory);
                    break;
                case 2:
                    crearTablas(connectionFactory);
                    break;
                case 3:
                    poblarMasivamente(connectionFactory);
                    break;

                case 4:
                    scanner.nextLine();
                    System.out.println("Introduce el nombre del atributo:");
                    String atributo = scanner.nextLine().trim();
                    System.out.println("Introduce el valor a buscar:");
                    String valor = scanner.nextLine().trim();
                    System.out.println("Seleccionar en tabla:");
                    System.out.println("1. Juego");
                    System.out.println("2. Mod");
                    System.out.println("3. Detalle");
                    System.out.println("4. Categoria");
                    System.out.print("Seleccione una opción: ");
                    int opcionTabla = scanner.nextInt();
                    scanner.nextLine();
                    switch (opcionTabla) {
                        case 1:
                            seleccionarConTextoJuego(connectionFactory, atributo, valor);
                            break;
                        case 2:
                            seleccionarConTextoMod(connectionFactory, atributo, valor);
                            break;
                        case 3:
                            seleccionarConTextoDetalle(connectionFactory, atributo, valor);
                            break;
                        case 4:
                            seleccionarConTextoCategoria(connectionFactory, atributo, valor);
                            break;
                        default:
                            System.out.println("Opción no válida.");
                    }
                    break;

                case 5:
                    scanner.nextLine(); // Limpiar el buffer de entrada
                    System.out.println("Introduce el nombre de la tabla en la que modificar el registro:");
                    String tablaModificar = scanner.nextLine().trim();
                    System.out.println("Introduce el ID del registro a modificar:");
                    int idRegistroModificar = scanner.nextInt();
                    modificarRegistro(connectionFactory, tablaModificar, idRegistroModificar);
                    break;

                case 6:
                    scanner.nextLine(); // Limpiar el buffer de entrada
                    System.out.println("Introduce el nombre de la tabla en la que modificar los registros:");
                    String tablaModificarRegistros = scanner.nextLine().trim();
                    String[] detallesModificar = solicitarDetallesModificar();
                    modificarRegistros(connectionFactory, tablaModificarRegistros, detallesModificar[0], detallesModificar[1], detallesModificar[2], detallesModificar[3]);
                    break;

                case 7:
                    scanner.nextLine(); // Limpiar el buffer de entrada
                    System.out.println("Introduce el nombre de la tabla en la que eliminar un registro:");
                    String tablaEliminarRegistro = scanner.nextLine().trim();
                    System.out.println("Introduce el ID del registro a eliminar:");
                    int idRegistroEliminar = scanner.nextInt();
                    eliminarRegistro(connectionFactory, tablaEliminarRegistro, idRegistroEliminar);
                    break;

                case 8:
                    scanner.nextLine(); // Limpiar el buffer de entrada
                    System.out.println("Introduce el nombre de la tabla en la que eliminar registros por condición:");
                    String tablaEliminarRegistrosCondicion = scanner.nextLine().trim();
                    System.out.println("Introduce el nombre del atributo para seleccionar los registros:");
                    String atributoEliminar = scanner.nextLine().trim();
                    System.out.println("Introduce el valor del atributo para seleccionar los registros:");
                    String valorEliminar = scanner.nextLine().trim();
                    eliminarRegistrosPorCondicion(connectionFactory, tablaEliminarRegistrosCondicion, atributoEliminar, valorEliminar);
                    break;

            }

        } while (opcion != 9);

        scanner.close();
    }


    private static void borrarTablas(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS Juego, Mod, Detalle, Categoria;");
            System.out.println("Tablas borradas exitosamente." + "\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    private static void crearTablas(ConnectionFactory connectionFactory) {
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

            System.out.println("Tablas creadas exitosamente."  + "\n");

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
            connectionFactory.disconnect();
        }
    }


    // Método para seleccionar elementos de la tabla Juego
    private static void seleccionarConTextoJuego(ConnectionFactory connectionFactory, String atributo, String valor) {
        seleccionarConTexto(connectionFactory, "Juego", atributo, valor);
    }

    // Método para seleccionar elementos de la tabla Mod
    private static void seleccionarConTextoMod(ConnectionFactory connectionFactory, String atributo, String valor) {
        seleccionarConTexto(connectionFactory, "Mod", atributo, valor);
    }

    // Método para seleccionar elementos de la tabla Detalle
    private static void seleccionarConTextoDetalle(ConnectionFactory connectionFactory, String atributo, String valor) {
        seleccionarConTexto(connectionFactory, "Detalle", atributo, valor);
    }

    // Método para seleccionar elementos de la tabla Categoria
    private static void seleccionarConTextoCategoria(ConnectionFactory connectionFactory, String atributo, String valor) {
        seleccionarConTexto(connectionFactory, "Categoria", atributo, valor);
    }

    // Método genérico para seleccionar elementos con texto de cualquier tabla
    private static void seleccionarConTexto(ConnectionFactory connectionFactory, String tabla, String atributo, String valor) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM " + tabla + " WHERE " + atributo + " = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Determinar el tipo de dato del valor y convertirlo apropiadamente
            if (isInteger(valor)) {
                preparedStatement.setInt(1, Integer.parseInt(valor));
            } else if (isDouble(valor)) {
                preparedStatement.setDouble(1, Double.parseDouble(valor));
            } else {
                preparedStatement.setString(1, valor);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterar sobre el ResultSet e imprimir los resultados
            while (resultSet.next()) {
                // Imprimir los valores de las columnas
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println(); // Salto de línea después de cada fila
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    // Método para verificar si un valor es un entero
    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para verificar si un valor es un número decimal
    private static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private static void modificarRegistro(ConnectionFactory connectionFactory, String tabla, int idRegistro) {
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
                Scanner scanner = new Scanner(System.in);
                System.out.println("\nIngrese los nuevos valores (presione Enter para mantener el valor actual):");
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
                System.out.println("Registro modificado exitosamente.");
            } else {
                System.out.println("El registro con ID " + idRegistro + " no existe en la tabla " + tabla + ".");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    private static void modificarRegistros(ConnectionFactory connectionFactory, String tabla, String atributo, String valor, String atributoModificar, String nuevoValor) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Consulta para seleccionar los registros específicos
            String query = "SELECT * FROM " + tabla + " WHERE " + atributo + " = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, valor);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Verificar si se encontraron registros
            if (resultSet.next()) {
                // Iterar sobre los resultados y modificar los registros
                do {
                    int idRegistro = resultSet.getInt(1); // Suponiendo que la columna 1 es la clave primaria

                    // Actualizar el campo deseado en cada registro
                    String updateQuery = "UPDATE " + tabla + " SET " + atributoModificar + " = ? WHERE " + tabla + "ID = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, nuevoValor);
                    updateStatement.setInt(2, idRegistro);
                    updateStatement.executeUpdate();
                } while (resultSet.next());

                System.out.println("Registros modificados exitosamente.");
            } else {
                System.out.println("No se encontraron registros que coincidan con los criterios proporcionados.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }

    private static String[] solicitarDetallesModificar() {
        Scanner scanner = new Scanner(System.in);
        String[] detalles = new String[4];

        System.out.println("Introduce el nombre del atributo para seleccionar los registros:");
        detalles[0] = scanner.nextLine().trim();
        System.out.println("Introduce el valor del atributo para seleccionar los registros:");
        detalles[1] = scanner.nextLine().trim();
        System.out.println("Introduce el nombre del atributo para modificar:");
        detalles[2] = scanner.nextLine().trim();
        System.out.println("Introduce el nuevo valor para el atributo:");
        detalles[3] = scanner.nextLine().trim();

        return detalles;
    }

    private static void eliminarRegistro(ConnectionFactory connectionFactory, String tabla, int idRegistro) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Consulta para eliminar el registro concreto
            String deleteQuery = "DELETE FROM " + tabla + " WHERE " + tabla + "ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, idRegistro);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Registro eliminado exitosamente.");
            } else {
                System.out.println("No se encontró ningún registro con el ID proporcionado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


    private static void eliminarRegistrosPorCondicion(ConnectionFactory connectionFactory, String tabla, String atributo, String valor) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {

            // Consulta para eliminar los registros que cumplan la condición
            String deleteQuery = "DELETE FROM " + tabla + " WHERE " + atributo + " = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, valor);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Registros eliminados exitosamente.");
            } else {
                System.out.println("No se encontraron registros que coincidan con los criterios proporcionados.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionFactory.disconnect();
        }
    }


}