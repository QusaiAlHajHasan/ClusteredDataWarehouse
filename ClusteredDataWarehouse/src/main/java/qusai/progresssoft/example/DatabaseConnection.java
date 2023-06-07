package qusai.progresssoft.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection() {
        connection = null;
    }

    public void connect(String DB_URL, String DB_UserName, String DB_Password) throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_UserName, DB_Password);
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        return connection != null;
    }
}


