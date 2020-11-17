package models;

import mappers.BookMapper;
import mappers.DBMapper;

import java.math.BigInteger;

public class Book extends DBModel {
    private BigInteger id;
    private String name;
    private String author;
    private BigInteger ausgeliehenVon;

    protected static DBMapper mapper = new BookMapper();

    public static DBMapper getMapper() {
        return mapper;
    }

    public Book() {
        id = BigInteger.valueOf(0);
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
