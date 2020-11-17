package relationshiptests;

import help.MappersHelper;
import helpers.HelpTesting;
import mappers.DBMapper;
import models.Book;
import models.User;
import models.relationships.BelongsTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class BelongsToTest{
    @BeforeEach
    void createTables() {
        HelpTesting.createTables();
    }

    @Test
    void queryRelationshipTest() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        DBMapper mapper = MappersHelper.bookMapper;
        DBMapper userMapper = MappersHelper.userMapper;

        Book b1 = (Book) mapper.getById(1);
        BelongsTo rel = new BelongsTo(Book.class, User.class, "ausgeliehenVon");
        assertNull(rel.queryRelationship(b1));
        assertNull(b1.getRelValue("owner"));

        Book b2 = (Book) mapper.getById(2);
        User u2 = (User) userMapper.getById(2);
        assertEquals(u2, rel.queryRelationship(b2));
        assertEquals(u2, b2.getRelValue("owner"));
    }
}
