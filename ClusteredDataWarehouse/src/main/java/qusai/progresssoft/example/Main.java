package qusai.progresssoft.example;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        final Logger LOGGER = Logger.getLogger(RequestHandling.class.getName());

        String DB_URL = "jdbc:mysql://localhost:3306/Clustered_Data_Warehouse";
        String DB_UserName = "root";
        String DB_Password = "0000";

        try {
            dbConnection.connect(DB_URL, DB_UserName, DB_Password);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Connection to the database is not established");
        } finally {
            try {
                dbConnection.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.log(Level.SEVERE, "DisConnection the database is not established");
            }
        }
    }
}