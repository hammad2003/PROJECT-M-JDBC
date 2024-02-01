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
import java.util.List;

public class PROJECTMenu {
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
//                                        // Insertar en la tabla Mod
//                                        try (PreparedStatement insertMod = connection.prepareStatement("INSERT INTO Mod (JuegoID, Nombre, Autor, Descripcion) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
//                                            insertMod.setInt(1, juegoID);
//                                            insertMod.setString(2, nombreMod);
//                                            insertMod.setString(3, autorMod);
//                                            insertMod.setString(4, descripcionMod);
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
}
