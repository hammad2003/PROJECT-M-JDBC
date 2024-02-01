package PROJECTM;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CopiaPROJECTMains {
//    private static void crearTablas(ConnectionFactory connectionFactory) {
//        try (Connection connection = connectionFactory.connect();
//             Statement statement = connection.createStatement()) {
//
//            // Sentencia SQL para crear la tabla Juego
//            String crearTablaJuego = "CREATE TABLE Juego (" +
//                    "JuegoID SERIAL PRIMARY KEY," +
//                    "Nombre VARCHAR(255) NOT NULL," +
//                    "Descripcion TEXT);";
//
//            // Sentencia SQL para crear la tabla Mod
//            String crearTablaMod = "CREATE TABLE Mod (" +
//                    "ModID SERIAL PRIMARY KEY," +
//                    "JuegoID INT," +
//                    "Nombre VARCHAR(255) NOT NULL," +
//                    "Autor VARCHAR(255) NOT NULL," +
//                    "Descripcion TEXT," +
//                    "Detalles JSONB," +  // Agregado el campo Detalles en formato JSONB
//                    "FOREIGN KEY (JuegoID) REFERENCES Juego(JuegoID));";
//
//            // Sentencia SQL para crear la tabla Categoria
//            String crearTablaCategoria = "CREATE TABLE Categoria (" +
//                    "CategoriaID SERIAL PRIMARY KEY," +
//                    "ModID INT," +
//                    "Nombre VARCHAR(255) NOT NULL," +
//                    "FOREIGN KEY (ModID) REFERENCES Mod(ModID));";
//
//            // Ejecutar las sentencias SQL
//            statement.executeUpdate(crearTablaJuego);
//            statement.executeUpdate(crearTablaMod);
//            statement.executeUpdate(crearTablaCategoria);
//
//            System.out.println("Tablas creadas exitosamente.");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            connectionFactory.disconnect();
//        }
//    }
//
//
//    public static void poblarMasivamente(ConnectionFactory connectionFactory) {
//        try (Connection connection = connectionFactory.connect()) {
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            InputSource inputSource = new InputSource(new FileReader("CurseForge.xml"));  // Usar FileReader para cargar desde un archivo
//            Document document = dBuilder.parse(inputSource);
//
//            document.getDocumentElement().normalize();
//
//            NodeList juegosNodeList = document.getElementsByTagName("Juego");
//
//            for (int temp = 0; temp < juegosNodeList.getLength(); temp++) {
//                Node juegoNode = juegosNodeList.item(temp);
//
//                if (juegoNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element juegoElement = (Element) juegoNode;
//
//                    String nombreJuego = juegoElement.getElementsByTagName("Nombre").item(0).getTextContent();
//                    String descripcionJuego = juegoElement.getElementsByTagName("Descripcion").item(0).getTextContent();
//
//                    // Insertar en la tabla Juego
//                    try (PreparedStatement insertJuego = connection.prepareStatement("INSERT INTO Juego (Nombre, Descripcion) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
//                        insertJuego.setString(1, nombreJuego);
//                        insertJuego.setString(2, descripcionJuego);
//                        insertJuego.executeUpdate();
//
//                        try (ResultSet juegoResultSet = insertJuego.getGeneratedKeys()) {
//                            if (juegoResultSet.next()) {
//                                int juegoID = juegoResultSet.getInt(1);
//
//                                // Insertar mods
//                                NodeList modsNodeList = juegoElement.getElementsByTagName("Mod");
//                                for (int modIndex = 0; modIndex < modsNodeList.getLength(); modIndex++) {
//                                    Node modNode = modsNodeList.item(modIndex);
//
//                                    if (modNode.getNodeType() == Node.ELEMENT_NODE) {
//                                        Element modElement = (Element) modNode;
//
//                                        String nombreMod = modElement.getElementsByTagName("Nombre").item(0).getTextContent();
//                                        String autorMod = modElement.getElementsByTagName("Autor").item(0).getTextContent();
//                                        String descripcionMod = modElement.getElementsByTagName("Descripcion").item(0).getTextContent();
//                                        // Obtener otros campos del Mod según sea necesario
//
//                                        List<String> detallesList = new ArrayList<>();  // Asegúrate de tener esta lista con los detalles
//                                        String detallesJSON = convertirDetallesAJSON(detallesList);
//
//                                        // Insertar en la tabla Mod
//                                        try (PreparedStatement insertMod = connection.prepareStatement("INSERT INTO Mod (JuegoID, Nombre, Autor, Descripcion, Detalles) VALUES (?, ?, ?, ?, ?::jsonb)", PreparedStatement.RETURN_GENERATED_KEYS)) {
//                                            // Insertar detalles (asumiendo que la lista de detalles está en el formato adecuado)
//                                            insertMod.setInt(1, juegoID);
//                                            insertMod.setString(2, nombreMod);
//                                            insertMod.setString(3, autorMod);
//                                            insertMod.setString(4, descripcionMod);
//                                            insertMod.setString(5, detallesJSON);
//
//                                            NodeList detallesNodeList = modElement.getElementsByTagName("Detalles");
//                                            if (detallesNodeList.getLength() > 0) {
//                                                Node detallesNode = detallesNodeList.item(0);
//                                                if (detallesNode.getNodeType() == Node.ELEMENT_NODE) {
//                                                    Element detallesElement = (Element) detallesNode;
//                                                    NodeList detalleNodes = detallesElement.getElementsByTagName("Detalle");
//
//
//                                                    for (int detalleIndex = 0; detalleIndex < detalleNodes.getLength(); detalleIndex++) {
//                                                        Node detalleNode = detalleNodes.item(detalleIndex);
//                                                        if (detalleNode.getNodeType() == Node.ELEMENT_NODE) {
//                                                            Element detalleElement = (Element) detalleNode;
//                                                            NodeList descripcionNodes = detalleElement.getElementsByTagName("Descripcion");
//
//                                                            // Verificar si la etiqueta <Descripcion> contiene texto
//                                                            if (descripcionNodes.getLength() > 0) {
//                                                                String descripcionDetalle = descripcionNodes.item(0).getTextContent();
//                                                                detallesList.add(descripcionDetalle);
//                                                            }
//                                                        }
//                                                    }
//                                                    // Convertir la lista de detalles a JSON
//                                                    insertMod.setString(5, detallesJSON);
//                                                }
//                                            }
//
//                                            // Insertar otros campos del Mod según sea necesario
//                                            insertMod.executeUpdate();
//
//                                            try (ResultSet modResultSet = insertMod.getGeneratedKeys()) {
//                                                if (modResultSet.next()) {
//                                                    int modID = modResultSet.getInt(1);
//
//                                                    // Insertar categorías
//                                                    NodeList categoriasNodeList = modElement.getElementsByTagName("Categoria");
//                                                    for (int categoriaIndex = 0; categoriaIndex < categoriasNodeList.getLength(); categoriaIndex++) {
//                                                        Node categoriaNode = categoriasNodeList.item(categoriaIndex);
//
//                                                        if (categoriaNode.getNodeType() == Node.ELEMENT_NODE) {
//                                                            Element categoriaElement = (Element) categoriaNode;
//                                                            String nombreCategoria = categoriaElement.getElementsByTagName("Nombre").item(0).getTextContent();
//
//                                                            // Insertar en la tabla Categoria
//                                                            try (PreparedStatement insertCategoria = connection.prepareStatement("INSERT INTO Categoria (ModID, Nombre) VALUES (?, ?)")) {
//                                                                insertCategoria.setInt(1, modID);
//                                                                insertCategoria.setString(2, nombreCategoria);
//                                                                insertCategoria.executeUpdate();
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            connectionFactory.disconnect();
//        }
//    }
//
//    private static String convertirDetallesAJSON(List<String> detallesList) {
//        StringBuilder jsonBuilder = new StringBuilder("[");
//
//        for (String detalle : detallesList) {
//            jsonBuilder.append("{\"Descripcion\":\"").append(detalle).append("\"},");
//        }
//
//        // Eliminar la última coma si hay al menos un detalle
//        if (!detallesList.isEmpty()) {
//            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
//        }
//
//        jsonBuilder.append("]");
//
//        return jsonBuilder.toString();
//    }
}