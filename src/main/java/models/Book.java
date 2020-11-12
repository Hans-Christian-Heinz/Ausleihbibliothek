package models;

import java.math.BigInteger;
import java.util.Map;

public class Book extends ARModel {
    private BigInteger id;

    @Override
    protected Map<String, String> getPropertyMap() {
        return null;
    }

    @Override
    protected String getTable() {
        return "books";
    }

    @Override
    public void setId(BigInteger id) {
        this.id = id;
    }
}
