package helpers;

import db.DatabaseHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HelpTesting {
    public static void createTables() {
        Connection db = DatabaseHelper.getConnection();

        String query = "DROP TABLE IF EXISTS users;";
        try {
            Statement stmt = db.createStatement();
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "DROP TABLE IF EXISTS books;";
        try {
            Statement stmt = db.createStatement();
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        query = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR NOT NULL," +
                "vorname VARCHAR NOT NULL," +
                "username VARCHAR NOT NULL," +
                "password VARCHAR NOT NULL," +
                "role VARCHAR" +
                ");";
        try {
            Statement stmt = db.createStatement();
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        query = "INSERT INTO users (name, vorname, username, password, role) VALUES(" +
                "'Mustermann'," +
                "'Max'," +
                "'m.mustermann'," +
                "'pwd'," +
                "NULL" +
                "),(" +
                "'Mustermann'," +
                "'Erika'," +
                "'e.mustermann'," +
                "'pwd'," +
                "'admin'" +
                ");";
        try {
            Statement stmt = db.createStatement();
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        query = "CREATE TABLE books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR NOT NULL," +
                "author VARCHAR NOT NULL," +
                "ausgeliehen_von INTEGER," +
                "FOREIGN KEY(ausgeliehen_von) REFERENCES users(id) ON DELETE CASCADE" +
                ");";
        try {
            Statement stmt = db.createStatement();
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        query = "INSERT INTO books (name, author, ausgeliehen_von) VALUES(" +
                "'Dune'," +
                "'Frank Herbert'," +
                "NULL" +
                "),(" +
                "'Rote Ernte'," +
                "'Eric Ambler'," +
                "2" +
                "),(" +
                "'Die Libelle'," +
                "'John le Carre'," +
                "2" +
                ");";
        try {
            Statement stmt = db.createStatement();
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
