package relationshiptests;

import db.DatabaseHelper;
import helpers.HelpTesting;
import mappers.DBMapper;
import models.Book;
import models.User;
import models.relationships.BelongsTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BelongsToTest{
    @BeforeEach
    void createTables() {
        HelpTesting.createTables();
    }

    @Test
    void queryRelationshipTest() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        DBMapper mapper = Book.getMapper();
        DBMapper userMapper = User.getMapper();

        Book b1 = (Book) mapper.getById(1);
        BelongsTo rel1 = new BelongsTo("owner", Book.class, User.class, "ausgeliehenVon");
        assertNull(rel1.queryRelationship(b1));

        Book b2 = (Book) mapper.getById(2);
        BelongsTo rel2 = new BelongsTo("owner", Book.class, User.class, "ausgeliehenVon");
        User u2 = (User) userMapper.getById(2);
        assertEquals(u2, rel2.queryRelationship(b2));
    }
}
