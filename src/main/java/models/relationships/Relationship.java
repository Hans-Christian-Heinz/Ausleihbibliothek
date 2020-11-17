package models.relationships;

import models.DBModel;

import java.sql.Connection;

public abstract class Relationship {
    protected DBModel owner;
    protected Class<? extends DBModel> otherClass;
    protected String ownFk;
    protected String otherFk;
    protected String name;
    protected Connection db;

    public abstract Object queryRelationship();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DBModel getOwner() {
        return owner;
    }

    public void setOwner(DBModel owner) {
        this.owner = owner;
    }

    public Class<? extends DBModel> getOtherClass() {
        return otherClass;
    }

    public void setOtherClass(Class<? extends DBModel> otherClass) {
        this.otherClass = otherClass;
    }

    public String getOwnFk() {
        return ownFk;
    }

    public void setOwnFk(String ownFk) {
        this.ownFk = ownFk;
    }

    public String getOtherFk() {
        return otherFk;
    }

    public void setOtherFk(String otherFk) {
        this.otherFk = otherFk;
    }
}
