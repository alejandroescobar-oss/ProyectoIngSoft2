package database;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final ConnectionProvider PROVIDER = new SQLiteConnectionProvider();

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return PROVIDER.getConnection();
    }
}