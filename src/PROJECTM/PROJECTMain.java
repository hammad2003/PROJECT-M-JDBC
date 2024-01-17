package PROJECTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                case 4:
                    seleccionarConTexto(connectionFactory);
                    break;
                case 5:
                    seleccionarConCondicion(connectionFactory);
                    break;
                case 6:
                    modificarRegistro(connectionFactory);
                    break;
            }

        } while (opcion != 7);

        scanner.close();
    }

    private static void borrarTablas(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS Juego, Mod, Categoria;");
            System.out.println("Tablas borradas exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void crearTablas(ConnectionFactory connectionFactory) {
        // Aquí deberías incluir las sentencias SQL para crear tus tablas
        // Utiliza try-with-resources para garantizar el cierre adecuado de los recursos
        try (Connection connection = connectionFactory.connect();
             Statement statement = connection.createStatement()) {
            // Añade aquí las sentencias SQL para crear tus tablas
            statement.executeUpdate("CREATE TABLE ...");
            System.out.println("Tablas creadas exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void poblarMasivamente(ConnectionFactory connectionFactory) {
        // Agrega lógica para poblar masivamente desde el CSV aquí.

        // Lee el CSV y obtén los datos (debes ajustar esto según tu estructura CSV).
        List<String[]> datosCSV = leerCSV("ruta/al/archivo.csv");

        // Conecta a la base de datos.
        Connection connection = connectionFactory.connect();

        try {
            // Prepara las sentencias SQL para insertar en las tablas.
            PreparedStatement insertJuego = connection.prepareStatement("INSERT INTO Juego(Nombre, Descripcion) VALUES (?, ?)");
            PreparedStatement insertMod = connection.prepareStatement("INSERT INTO Mod(JuegoID, Nombre, Autor, Descripcion, Version, FechaSubida, FechaActualizacion, Tamano, VersionMinecraft, RequisitoForge, Downloads) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement insertCategoria = connection.prepareStatement("INSERT INTO Categoria(ModID, Nombre) VALUES (?, ?)");

            // Itera sobre los datos del CSV y ejecuta las inserciones.
            for (String[] fila : datosCSV) {
                // Puedes ajustar esto según la estructura específica de tu CSV.
                String nombreJuego = fila[0];
                String descripcionJuego = fila[1];
                String nombreMod = fila[2];
                String autorMod = fila[3];
                String descripcionMod = fila[4];
                String detalleMod = fila[5];
                String categoriaMod = fila[6];

                // Inserta en la tabla Juego.
                insertJuego.setString(1, nombreJuego);
                insertJuego.setString(2, descripcionJuego);
                insertJuego.executeUpdate();

                // Recupera el ID del juego recién insertado.
                ResultSet juegoResultSet = insertJuego.getGeneratedKeys();
                juegoResultSet.next();
                int juegoID = juegoResultSet.getInt(1);

                // Inserta en la tabla Mod.
                insertMod.setInt(1, juegoID);
                insertMod.setString(2, nombreMod);
                insertMod.setString(3, autorMod);
                insertMod.setString(4, descripcionMod);
                // ... (continúa con los demás campos)
                insertMod.executeUpdate();

                // Recupera el ID del mod recién insertado.
                ResultSet modResultSet = insertMod.getGeneratedKeys();
                modResultSet.next();
                int modID = modResultSet.getInt(1);

                // Inserta en la tabla Categoria.
                insertCategoria.setInt(1, modID);
                insertCategoria.setString(2, categoriaMod);
                insertCategoria.executeUpdate();
            }

            System.out.println("Datos insertados exitosamente desde el CSV.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cierra las conexiones y los recursos.
            connectionFactory.disconnect();
        }
    }

    private static List<String[]> leerCSV(String rutaArchivo) {
        // Aquí debes implementar la lógica para leer el CSV.
        // Puedes usar OpenCSV u otra biblioteca de tu elección.
        // Devuelve una lista de arrays de cadenas, donde cada array representa una fila del CSV.
        return null;
    }


    private static void seleccionarConTexto(ConnectionFactory connectionFactory) {
        // Lógica para seleccionar elementos que contengan un texto concreto
        // Puedes utilizar PreparedStatement para evitar problemas de SQL injection
        try (Connection connection = connectionFactory.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Juego WHERE Descripcion LIKE ?")) {
            // Setea el parámetro del PreparedStatement con el texto deseado
            preparedStatement.setString(1, "%texto_a_buscar%");
            // Ejecuta la consulta
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Procesa los resultados
                while (resultSet.next()) {
                    System.out.println("Juego ID: " + resultSet.getInt("JuegoID"));
                    System.out.println("Nombre: " + resultSet.getString("Nombre"));
                    System.out.println("Descripción: " + resultSet.getString("Descripcion"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seleccionarConCondicion(ConnectionFactory connectionFactory) {
        // Lógica para seleccionar elementos que cumplan una condición
        // Puedes utilizar PreparedStatement para evitar problemas de SQL injection
        try (Connection connection = connectionFactory.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Juego WHERE alguna_condicion = ?")) {
            // Setea el parámetro del PreparedStatement con el valor deseado
            preparedStatement.setString(1, "valor");
            // Ejecuta la consulta
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Procesa los resultados
                while (resultSet.next()) {
                    System.out.println("Juego ID: " + resultSet.getInt("JuegoID"));
                    System.out.println("Nombre: " + resultSet.getString("Nombre"));
                    System.out.println("Descripción: " + resultSet.getString("Descripcion"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void modificarRegistro(ConnectionFactory connectionFactory) {
        // Lógica para modificar un registro concreto
        // Puedes utilizar PreparedStatement para evitar problemas de SQL injection
        try (Connection connection = connectionFactory.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Juego SET Nombre = ?, Descripcion = ? WHERE JuegoID = ?")) {
            // Setea los parámetros del PreparedStatement con los nuevos valores
            preparedStatement.setString(1, "NuevoNombre");
            preparedStatement.setString(2, "NuevaDescripcion");
            preparedStatement.setInt(1, 123);
            // Ejecuta la actualización
            int filasActualizadas = preparedStatement.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Registro modificado exitosamente.");
            } else {
                System.out.println("No se encontró el registro a modificar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}