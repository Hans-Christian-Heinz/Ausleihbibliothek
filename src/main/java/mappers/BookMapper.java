package mappers;

import models.Book;
import models.DBModel;

import java.util.HashMap;
import java.util.Map;

public class BookMapper extends DBMapper {
    @Override
    protected Class<? extends DBModel> stdClass() {
        return Book.class;
    }

    @Override
    protected String stdTable() {
        return "books";
    }

    @Override
    protected Map<String, String> stdPropertyMap() {
        Map<String, String> res = new HashMap<>();
        res.put("id", "id");
        res.put("author", "author");
        res.put("name", "name");
        res.put("ausgeliehenVon", "ausgeliehen_von");

        return res;
    }
}
