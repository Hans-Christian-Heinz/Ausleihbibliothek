package trlationshiptests;

import db.DatabaseHelper;
import mappers.BookMapper;
import mappers.DBMapper;
import mappers.UserMapper;
import models.Book;
import models.User;
import models.relationships.BelongsTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BelongsToTest {
    private Connection db;

    public BelongsToTest() {
        //String dbPath = "../resources/db_test.sqlite";
        String dbPath = "/home/h.heinz/IdeaProjects/Ausleihbibliothek/src/test/resources/db_test.sqlite";
        db = DatabaseHelper.getConnection();
    }

    @BeforeEach
    void createTables() {
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

    @Test
    void queryRelationshipTest() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        DBMapper mapper = Book.getMapper();
        DBMapper userMapper = User.getMapper();

        Book b1 = (Book) mapper.getById(1);
        BelongsTo rel1 = new BelongsTo("owner", b1, User.class, "ausgeliehenVon");
        assertNull(rel1.queryRelationship());

        Book b2 = (Book) mapper.getById(2);
        BelongsTo rel2 = new BelongsTo("owner", b2, User.class, "ausgeliehenVon");
        User u2 = (User) userMapper.getById(2);
        assertEquals(u2, rel2.queryRelationship());
    }
}
