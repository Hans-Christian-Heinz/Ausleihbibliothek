package mappertests;

import mappers.UserMapper;
import models.DBModel;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserMapperTest {
    private final UserMapper mapper;
    private Connection db;

    public UserMapperTest() {
        mapper = new UserMapper();
        //String dbPath = "../resources/db_test.sqlite";
        String dbPath = "/home/h.heinz/IdeaProjects/Ausleihbibliothek/src/test/resources/db_test.sqlite";
        try {
            Class.forName("org.sqlite.JDBC");
            db = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void createTable() {
        String query = "DROP TABLE IF EXISTS users;";
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
    }

    @Test
    void getById() {
        try {
            DBModel user = mapper.getById(1, db);
            User u2 = new User();
            u2.setVorname("Max");
            u2.setName("Mustermann");
            u2.setUsername("m.mustermann");
            u2.setPassword("pwd");
            u2.setId(BigInteger.valueOf(1));
            assertTrue(user instanceof User);
            assertEquals(user, u2);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getByUsername() {
        try {
            DBModel user = mapper.getByKey("username", "e.mustermann", db);
            User u2 = new User();
            u2.setVorname("Erika");
            u2.setName("Mustermann");
            u2.setUsername("e.mustermann");
            u2.setPassword("pwd");
            u2.setRole("admin");
            u2.setId(BigInteger.valueOf(2));
            assertTrue(user instanceof User);
            assertEquals(user, u2);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAll() {
        List<DBModel> liste = mapper.getAll(db);
        assertEquals(2, liste.size());

        User u1 = new User();
        u1.setVorname("Max");
        u1.setName("Mustermann");
        u1.setUsername("m.mustermann");
        u1.setPassword("pwd");
        u1.setId(BigInteger.valueOf(1));
        User u2 = new User();
        u2.setVorname("Erika");
        u2.setName("Mustermann");
        u2.setUsername("e.mustermann");
        u2.setPassword("pwd");
        u2.setRole("admin");
        u2.setId(BigInteger.valueOf(2));

        for (DBModel model : liste) {
            assertTrue(model instanceof User);
            if (model.getId().equals(BigInteger.valueOf(2))) {
                assertEquals(model, u2);
            }
            else {
                assertEquals(model, u1);
            }
        }
    }

    @Test
    void insert() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException, InstantiationException, SQLException {
        User user = new User();
        user.setPassword("pwd");
        user.setUsername("a.xyz");
        user.setName("Xyz");
        user.setVorname("Abc");
        user.setRole("admin");

        mapper.insert(db, user);
        assertEquals(mapper.getById(3, db), user);
    }

    @Test
    void update() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        User user = (User) mapper.getById(1, db);
        user.setVorname("Maximilian");
        mapper.update(db, user);
        assertEquals(user, mapper.getById(1, db));
    }

    @Test
    void delete() {
        mapper.delete(db, BigInteger.valueOf(1));
        List<DBModel> liste = mapper.getAll(db);
        assertEquals(1, liste.size());
        User u2 = new User();
        u2.setVorname("Erika");
        u2.setName("Mustermann");
        u2.setUsername("e.mustermann");
        u2.setPassword("pwd");
        u2.setRole("admin");
        u2.setId(BigInteger.valueOf(2));
        assertEquals(u2, liste.get(0));
    }
}
