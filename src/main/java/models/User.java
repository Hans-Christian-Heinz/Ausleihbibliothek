package models;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class User extends ARModel {
    private BigInteger id;
    private String username;
    private String name;
    private String vorname;
    private String role;
    private String password;

    @Override
    public Map<String, String> getPropertyMap() {
        Map<String, String> res = new HashMap<>();
        res.put("id", "id");
        res.put("username", "username");
        res.put("vorname", "vorname");
        res.put("name", "name");
        res.put("role", "role");
        res.put("password", "password");
        return res;
    }

    @Override
    public String getTable() {
        return "users";
    }

    public User() {
        id = BigInteger.valueOf(0);
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
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
        return vorname + " " + name;
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
}
