package database;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initialize() {

        String sql = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    login TEXT NOT NULL UNIQUE,
                    nombre_completo TEXT NOT NULL,
                    rol TEXT NOT NULL,
                    estado TEXT NOT NULL,
                    password_hash TEXT NOT NULL
                )
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement()
        ) {

            statement.execute(sql);

            System.out.println("Base de datos inicializada correctamente.");

        } catch (SQLException e) {

            System.err.println(
                    "Error al inicializar la base de datos: "
                            + e.getMessage()
            );
        }
    }
}