package models.relationships;

import exceptions.DBMapperException;
import models.DBModel;

public abstract class Relationship {
    protected Class<? extends DBModel> ownerClass;
    protected Class<? extends DBModel> otherClass;
    protected String ownFk;
    protected String otherFk;

    public abstract Object queryRelationship(DBModel owner) throws DBMapperException;
    //public abstract String getSqlJoin();

    public Class<? extends DBModel> getOwnerClass() {
        return ownerClass;
    }

    public void setOwner(Class<? extends DBModel> ownerClass) {
        this.ownerClass = ownerClass;
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
