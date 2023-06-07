package qusai.progresssoft.example;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandling {
    private String dealUniqueId;
    private String fromCurrencyISOCode;
    private String toCurrencyISOCode;
    private Timestamp dealTimestamp;
    private BigDecimal dealAmount;

    private static final Logger LOGGER = Logger.getLogger(RequestHandling.class.getName());

    public RequestHandling(String dealUniqueId, String fromCurrencyISOCode, String toCurrencyISOCode,
                       Timestamp dealTimestamp, BigDecimal dealAmount) {
        this.dealUniqueId = dealUniqueId;
        this.fromCurrencyISOCode = fromCurrencyISOCode;
        this.toCurrencyISOCode = toCurrencyISOCode;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
    }

    public String getDealUniqueId() {
        return dealUniqueId;
    }

    public void setDealUniqueId(String dealUniqueId) {
        this.dealUniqueId = dealUniqueId;
    }

    public String getFromCurrencyISOCode() {
        return fromCurrencyISOCode;
    }

    public void setFromCurrencyISOCode(String fromCurrencyISOCode) {
        this.fromCurrencyISOCode = fromCurrencyISOCode;
    }

    public String getToCurrencyISOCode() {
        return toCurrencyISOCode;
    }

    public void setToCurrencyISOCode(String toCurrencyISOCode) {
        this.toCurrencyISOCode = toCurrencyISOCode;
    }

    public Timestamp getDealTimestamp() {
        return dealTimestamp;
    }

    public void setDealTimestamp(Timestamp dealTimestamp) {
        this.dealTimestamp = dealTimestamp;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }

    public boolean isValid() {
        if (dealUniqueId == null || dealUniqueId.isEmpty()) {
            return false;
        }
        if (fromCurrencyISOCode == null || fromCurrencyISOCode.isEmpty()) {
            return false;
        }
        if (toCurrencyISOCode == null || toCurrencyISOCode.isEmpty()) {
            return false;
        }
        if (dealTimestamp == null) {
            return false;
        }
        if (dealAmount == null) {
            return false;
        }

        return true;
    }

    public boolean isDuplicateRequest(DatabaseConnection dbConnection) {
        try {
            String query = "SELECT COUNT(*) FROM deals WHERE dealUniqueId = ?";
            PreparedStatement statement = dbConnection.getConnection().prepareStatement(query);
            statement.setString(1, dealUniqueId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean persist(DatabaseConnection dbConnection) throws SQLException {
        if (!dbConnection.isConnected()) {
            LOGGER.log(Level.SEVERE, "Connection to the database is not established");
            throw new SQLException("Connection to the database is not established");
        }

        try {
            String query = "INSERT INTO deals (dealUniqueId, fromCurrencyISOCode, toCurrencyISOCode, dealTimestamp, dealAmount) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = null;
            try {
                statement = dbConnection.getConnection().prepareStatement(query);
                statement.setString(1, dealUniqueId);
                statement.setString(2, fromCurrencyISOCode);
                statement.setString(3, toCurrencyISOCode);
                statement.setTimestamp(4, dealTimestamp);
                statement.setBigDecimal(5, dealAmount);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    LOGGER.log(Level.INFO, "Handling request persisted successfully");
                } else {
                    LOGGER.log(Level.INFO, "Handling request persisted failed");
                }
                return rowsAffected > 0;
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while persisting the deal request", e);
            throw e;
        }
    }
}
