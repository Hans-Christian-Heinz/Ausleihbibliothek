package relationshiptests;

import help.MappersHelper;
import helpers.HelpTesting;
import mappers.UserMapper;
import models.Book;
import models.DBModel;
import models.User;
import models.relationships.HasMany;
import models.relationships.Relationship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HasManyTest {
    @BeforeEach
    void createTables() {
        HelpTesting.createTables();
    }

    @Test
    void queryRelationshipTest() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Relationship rel = new HasMany(User.class, Book.class, "ausgeliehenVon");
        UserMapper mapper = MappersHelper.userMapper;
        DBModel u1 = mapper.getById(1);
        DBModel u2 = mapper.getById(2);

        List<DBModel> l1 = (List<DBModel>) rel.queryRelationship(u1);
        List<DBModel> l2 = (List<DBModel>) rel.queryRelationship(u2);
        List<DBModel> la = (List<DBModel>) u1.getRelValue("books");
        List<DBModel> lb = (List<DBModel>) u2.getRelValue("books");
        Book b2 = new Book();
        b2.setName("Rote Ernte");
        b2.setAuthor("Eric Ambler");
        b2.setAusgeliehenVon(BigInteger.valueOf(2));
        b2.setId(BigInteger.valueOf(2));
        Book b3 = new Book();
        b3.setName("Die Libelle");
        b3.setAuthor("John le Carre");
        b3.setAusgeliehenVon(BigInteger.valueOf(2));
        b3.setId(BigInteger.valueOf(3));

        assertEquals(0, l1.size());
        assertEquals(2, l2.size());
        assertEquals(b2, l2.get(0));
        assertEquals(b3, l2.get(1));
        assertEquals(0, la.size());
        assertEquals(2, lb.size());
        assertEquals(b2, lb.get(0));
        assertEquals(b3, lb.get(1));
    }
}
