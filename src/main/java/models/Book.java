package models;

import java.math.BigInteger;

public class Book extends DBModel {
    private BigInteger id;

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
