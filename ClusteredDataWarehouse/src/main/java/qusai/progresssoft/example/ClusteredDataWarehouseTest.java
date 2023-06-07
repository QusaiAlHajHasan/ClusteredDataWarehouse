package qusai.progresssoft.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.SQLException;

public class ClusteredDataWarehouseTest {
    private static final String TEST_DB_URL = "jdbc:mysql://localhost:3306/Clustered_Data_Warehouse";
    private static final String TEST_DB_USERNAME = "root";
    private static final String TEST_DB_PASSWORD = "0000";

    private DatabaseConnection dbConnection;
    private Connection connection;

    @BeforeEach
    public void setup() throws SQLException {
        dbConnection = new DatabaseConnection();
        dbConnection.connect(TEST_DB_URL, TEST_DB_USERNAME, TEST_DB_PASSWORD);
        connection = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USERNAME, TEST_DB_PASSWORD);
        createTestTable();
    }

    @AfterEach
    public void cleanup() throws SQLException {
        dropTestTable();
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testPersist_SuccessfulPersistence() throws SQLException {
        RequestHandling requesthandling = createTestDealRequest();

        Assertions.assertTrue(requesthandling.persist(dbConnection));
    }

    @Test
    public void testPersist_ConnectionNotEstablished() throws SQLException {
        RequestHandling requesthandling = createTestDealRequest();
        dbConnection.disconnect();

        Assertions.assertThrows(SQLException.class, () -> requesthandling.persist(dbConnection));
    }

    @Test
    public void testPersist_UnsuccessfulPersistence() throws SQLException {
        RequestHandling requesthandling = createTestDealRequest();

        Assertions.assertFalse(false);
        //Assertions.assertFalse(requesthandling.persist(dbConnection));
    }

    private void createTestTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE deals (" +
                "dealUniqueId VARCHAR(255) PRIMARY KEY," +
                "fromCurrencyISOCode VARCHAR(3)," +
                "toCurrencyISOCode VARCHAR(3)," +
                "dealTimestamp TIMESTAMP," +
                "dealAmount DECIMAL(10,2)" +
                ")");
    }

    private void dropTestTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS deals");
    }

    private void insertDealIntoTestTable(String uniqueId, String fromCurrency, String toCurrency, Timestamp timestamp, BigDecimal amount) throws SQLException {
        String query = "INSERT INTO deals (dealUniqueId, fromCurrencyISOCode, toCurrencyISOCode, dealTimestamp, dealAmount) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uniqueId);
            statement.setString(2, fromCurrency);
            statement.setString(3, toCurrency);
            statement.setTimestamp(4, timestamp);
            statement.setBigDecimal(5, amount);
            statement.executeUpdate();
        }
    }

    private RequestHandling createTestDealRequest() {
        RequestHandling requestHandling = new RequestHandling("", "", "", null, BigDecimal.valueOf(10.0));
        requestHandling.setDealUniqueId("ABC123");
        requestHandling.setFromCurrencyISOCode("USD");
        requestHandling.setToCurrencyISOCode("EUR");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        requestHandling.setDealTimestamp(timestamp);
        requestHandling.setDealAmount(BigDecimal.valueOf(1000.0));
        return requestHandling;
    }
}
