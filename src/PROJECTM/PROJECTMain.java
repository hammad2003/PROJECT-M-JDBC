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
            System.out.println("3. Poblar masivamente las tablas desde el CSV");
            System.out.println("4. Seleccionar todos los elementos que contengan un texto concreto");
            System.out.println("5. Seleccionar todos los elementos que cumplan una condición");
            System.out.println("6. Seleccionar un elemento concreto y permitir su modificación");
            System.out.println("7. Salir");
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
//                case 4:
//                    seleccionarConTexto(connectionFactory);
//                    break;
//                case 5:
//                    seleccionarConCondicion(connectionFactory);
//                    break;
//                case 6:
//                    modificarRegistro(connectionFactory);
//                    break;
            }

        } while (opcion != 7);

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


}