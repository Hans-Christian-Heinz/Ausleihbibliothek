package mappertests;

import db.DatabaseHelper;
import help.MappersHelper;
import helpers.HelpTesting;
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

    public UserMapperTest() {
        mapper = MappersHelper.userMapper;
    }

    @BeforeEach
    void createTables() {
        HelpTesting.createTables();
    }

    @Test
    void getById() {
        try {
            DBModel user = mapper.getById(1);
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
            DBModel user = mapper.getByKey("username", "e.mustermann");
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
        List<DBModel> liste = mapper.getAll();
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
    void getAllWhereIndex() {
        User u2 = new User();
        u2.setVorname("Erika");
        u2.setName("Mustermann");
        u2.setUsername("e.mustermann");
        u2.setPassword("pwd");
        u2.setRole("admin");
        u2.setId(BigInteger.valueOf(2));

        List<DBModel> liste = mapper.getAllWhereIndex("username", "e.mustermann");
        assertEquals(1, liste.size());
        assertEquals(u2, liste.get(0));
    }

    @Test
    void insert() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException, InstantiationException, SQLException {
        User user = new User();
        user.setPassword("pwd");
        user.setUsername("a.xyz");
        user.setName("Xyz");
        user.setVorname("Abc");
        user.setRole("admin");

        mapper.insert(user);
        assertEquals(mapper.getById(3), user);
    }

    @Test
    void update() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        User user = (User) mapper.getById(1);
        user.setVorname("Maximilian");
        mapper.update(user);
        assertEquals(user, mapper.getById(1));
    }

    @Test
    void delete() {
        mapper.delete(BigInteger.valueOf(1));
        List<DBModel> liste = mapper.getAll();
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
