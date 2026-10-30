package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class SQLiteConnectionProvider implements ConnectionProvider {
    private final String databaseUrl;

    public SQLiteConnectionProvider() {
        this("jdbc:sqlite:taller2.db");
    }

    public SQLiteConnectionProvider(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }
}
