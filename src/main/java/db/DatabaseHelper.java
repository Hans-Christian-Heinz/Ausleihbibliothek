package db;

import java.sql.*;
import java.util.Properties;

public final class DatabaseHelper {
    private static Connection db;

    /**
     * @return Die Verbindung zur Datenbank.
     */
    public static Connection getConnection(){
        if (db == null) {
            try {
                if (isJUnitTest()) {
                    String dbPath = "/home/h.heinz/IdeaProjects/Ausleihbibliothek/src/test/resources/db_test.sqlite";
                    Class.forName("org.sqlite.JDBC");
                    db = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                }
                else {
                    Class.forName("org.mariadb.jdbc.Driver");
                    Properties props = new Properties();
                    props.put("user", "bibliothek");
                    props.put("password", "Test1234");
                    db = DriverManager.getConnection("jdbc:mariadb://localhost:3306/bibliothek", props);
                }
            }
            catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        return db;
    }

    /**
     * different database for JUnit
     *
     * @return
     */
    private static boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

}
