package models;

import help.MappersHelper;
import mappers.DBMapper;
import models.relationships.BelongsTo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Book extends DBModel {
    private BigInteger id;
    private String name;
    private String author;
    private BigInteger ausgeliehenVon;

    public Book() {
        id = BigInteger.valueOf(0);
        relationships = Map.of(
                "owner", new BelongsTo(Book.class, User.class, "ausgeliehenVon")
        );
        relValues = new HashMap<>();
        relValues.put("owner", "");
    }

    public static DBMapper getMapper() {
        return MappersHelper.bookMapper;
    }

    @Override
    public String getTable() {
        return MappersHelper.bookMapper.getTable();
    }

    @Override
    public Map<String, String> getPropertyMap() {
        return MappersHelper.bookMapper.getPropertyMap();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigInteger getAusgeliehenVon() {
        return ausgeliehenVon;
    }

    public void setAusgeliehenVon(BigInteger ausgeliehenVon) {
        this.ausgeliehenVon = ausgeliehenVon;
    }

    public void setAusgeliehenVon(Integer ausgeliehenVon) {
        this.ausgeliehenVon = BigInteger.valueOf((ausgeliehenVon.longValue()));
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            Book b2 = (Book) obj;
            boolean eq = true;

            if (name == null) {
                eq = eq && b2.name == null;
            }
            else {
                eq = eq && name.equals(b2.name);
            }
            if (author == null) {
                eq = eq && b2.author == null;
            }
            else {
                eq = eq && author.equals(b2.author);
            }
            if (ausgeliehenVon == null) {
                eq = eq && b2.ausgeliehenVon == null;
            }
            else {
                eq = eq && ausgeliehenVon.equals(b2.ausgeliehenVon);
            }

            return eq;
        }

        return false;
    }
}
