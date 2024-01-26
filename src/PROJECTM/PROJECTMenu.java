package PROJECTM;

import java.sql.*;
import java.util.List;

public class PROJECTMenu {

//    private static void seleccionarConCondicion(ConnectionFactory connectionFactory) {
//        // Lógica para seleccionar elementos que cumplan una condición
//        // Puedes utilizar PreparedStatement para evitar problemas de SQL injection
//        try (Connection connection = connectionFactory.connect();
//             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Juego WHERE alguna_condicion = ?")) {
//            // Setea el parámetro del PreparedStatement con el valor deseado
//            preparedStatement.setString(1, "valor");
//            // Ejecuta la consulta
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                // Procesa los resultados
//                while (resultSet.next()) {
//                    System.out.println("Juego ID: " + resultSet.getInt("JuegoID"));
//                    System.out.println("Nombre: " + resultSet.getString("Nombre"));
//                    System.out.println("Descripción: " + resultSet.getString("Descripcion"));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void modificarRegistro(ConnectionFactory connectionFactory) {
//        // Lógica para modificar un registro concreto
//        // Puedes utilizar PreparedStatement para evitar problemas de SQL injection
//        try (Connection connection = connectionFactory.connect();
//             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Juego SET Nombre = ?, Descripcion = ? WHERE JuegoID = ?")) {
//            // Setea los parámetros del PreparedStatement con los nuevos valores
//            preparedStatement.setString(1, "NuevoNombre");
//            preparedStatement.setString(2, "NuevaDescripcion");
//            preparedStatement.setInt(1, 123);
//            // Ejecuta la actualización
//            int filasActualizadas = preparedStatement.executeUpdate();
//            if (filasActualizadas > 0) {
//                System.out.println("Registro modificado exitosamente.");
//            } else {
//                System.out.println("No se encontró el registro a modificar.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
