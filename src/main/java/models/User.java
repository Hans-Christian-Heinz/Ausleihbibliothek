package models;

import help.MappersHelper;
import mappers.DBMapper;
import models.relationships.HasMany;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User extends DBModel {
    private BigInteger id;
    private String username;
    private String name;
    private String vorname;
    private String role;
    private String password;

    public User() {
        id = BigInteger.valueOf(0);
        relationships = Map.of(
                "books", new HasMany(User.class, Book.class, "ausgeliehenVon")
        );
        relValues = new HashMap<>();
        relValues.put("books", "");
    }

    public static DBMapper getMapper() {
        return MappersHelper.userMapper;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setId(Integer id) {
        setId(BigInteger.valueOf(id.longValue()));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public String getFullName() {
        String res;
        res = Objects.requireNonNullElse(vorname, "");
        if (name != null)
            res += " " + name;
        return res;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", vorname='" + vorname + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        //return super.equals(obj);
        if (obj instanceof User) {
            User u2 = (User) obj;
            boolean eq = true;
            if (name == null) {
                eq = eq && u2.name == null;
            }
            else {
                eq = eq && this.getName().equals(u2.getName());
            }
            if (vorname == null) {
                eq = eq && u2.vorname == null;
            }
            else {
                eq = eq && vorname.equals(u2.vorname);
            }
            if (username == null) {
                eq = eq && u2.username == null;
            }
            else {
                eq = eq && this.username.equals(u2.username);
            }
            if (password == null) {
                eq = eq && u2.password == null;
            }
            else {
                eq = eq && this.password.equals(u2.password);
            }
            if (role == null) {
                eq = eq && u2.role == null;
            }
            else {
                eq = eq && this.role.equals(u2.role);
            }
            return eq;
        }
        else {
            return false;
        }
    }
}
