package mappertests;

import db.DatabaseHelper;
import exceptions.DBMapperException;
import help.MappersHelper;
import helpers.HelpTesting;
import mappers.BookMapper;
import models.Book;
import models.DBModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookMapperTest {
    private final BookMapper mapper;
    private final Connection db;

    public BookMapperTest() {
        mapper = MappersHelper.bookMapper;
        db = DatabaseHelper.getConnection();
    }

    @BeforeEach
    void createTables() {
        HelpTesting.createTables();
    }

    @Test
    void getById() {
        try {
            DBModel book = mapper.getById(1);
            Book b2 = new Book();
            b2.setName("Dune");
            b2.setAuthor("Frank Herbert");
            b2.setId(BigInteger.valueOf(1));
            assertTrue(book instanceof Book);
            assertEquals(book, b2);
        } catch (DBMapperException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAll() {
        List<DBModel> liste = mapper.getAll();
        assertEquals(3, liste.size());

        Book b1 = new Book();
        b1.setName("Dune");
        b1.setAuthor("Frank Herbert");
        b1.setId(BigInteger.valueOf(1));
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

        for (DBModel model : liste) {
            assertTrue(model instanceof Book);
            if (model.getId().equals(BigInteger.valueOf(2))) {
                assertEquals(model, b2);
            }
            else if(model.getId().equals(BigInteger.valueOf(1))) {
                assertEquals(model, b1);
            }
            else {
                assertEquals(model, b3);
            }
        }
    }

    @Test
    void getAllWhereIndex() {
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

        List<DBModel> liste = mapper.getAllWhereIndex("ausgeliehenVon", "2");
        assertEquals(2, liste.size());
        assertEquals(b2, liste.get(0));
        assertEquals(b3, liste.get(1));
    }

    @Test
    void insert() {
        Book book = new Book();
        book.setAuthor("Robert Harris");
        book.setName("Das Schweigen der Lämmer");

        try {
            mapper.insert(book);
        } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(mapper.getById(4), book);
        } catch (DBMapperException e) {
            e.printStackTrace();
        }
    }

    @Test
    void update() {
        Book book = null;
        try {
            book = (Book) mapper.getById(2);
        } catch (DBMapperException e) {
            e.printStackTrace();
        }
        book.setAuthor("Dashiell Hammit");
        try {
            mapper.update(book);
        } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(book, mapper.getById(2));
        } catch (DBMapperException e) {
            e.printStackTrace();
        }
    }

    @Test
    void delete() {
        mapper.delete(BigInteger.valueOf(1));
        List<DBModel> liste = mapper.getAll();
        assertEquals(2, liste.size());
        Book b2 = new Book();
        b2.setAuthor("Eric Ambler");
        b2.setName("Rote Ernte");
        b2.setId(BigInteger.valueOf(2));
        b2.setAusgeliehenVon(BigInteger.valueOf(2));
        Book b3 = new Book();
        b3.setAuthor("John le Carre");
        b3.setName("Die Libelle");
        b3.setId(BigInteger.valueOf(3));
        b3.setAusgeliehenVon(BigInteger.valueOf(2));
        assertEquals(b3, liste.get(1));
    }
}
