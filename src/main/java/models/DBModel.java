package models;

import mappers.DBMapper;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

public abstract class DBModel implements Serializable {
    protected static DBMapper mapper;

    public static DBMapper getMapper() {
        return mapper;
    }

    public abstract void setId(BigInteger id);
    public abstract BigInteger getId();
    public abstract void setId(Integer id);

    public Map<String, String> getPropertyMap() {
        return mapper.getPropertyMap();
    }
    public String getTable() {
        return mapper.getTable();
    }
}
