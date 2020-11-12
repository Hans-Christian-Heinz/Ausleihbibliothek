package db;

import java.sql.*;
import java.util.Properties;

public class DatabaseHelper {
    /**
     * @return Die Verbindung zur Datenbank.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        Properties props = new Properties();
        props.put("user", "bibliothek");
        props.put("password", "Test1234");
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/bibliothek", props);
    }
}
