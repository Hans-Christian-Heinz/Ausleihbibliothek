package models;

import java.math.BigInteger;
import java.util.Map;

public class Book extends ARModel {
    private BigInteger id;

    @Override
    public Map<String, String> getPropertyMap() {
        return null;
    }

    @Override
    public String getTable() {
        return "books";
    }

    @Override
    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public void setId(Integer id) {
        this.id = BigInteger.valueOf(id.longValue());
    }

    @Override
    public BigInteger getId() {
        return id;
    }
}
