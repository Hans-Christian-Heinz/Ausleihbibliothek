package models;

import models.relationships.Relationship;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

public abstract class DBModel implements Serializable {
    protected Map<String, Relationship> relationships = Map.of();
    protected Map<String, Object> relValues = Map.of();

    public abstract void setId(BigInteger id);
    public abstract BigInteger getId();
    public abstract void setId(Integer id);
    public abstract String getTable();
    public abstract Map<String, String> getPropertyMap();

    private Object queryRelationship(String relName) {
        Relationship rel = relationships.get(relName);
        if (rel == null) {
            return null;
        }
        return rel.queryRelationship(this);
    }

    public Object getRelValue(String relName) {
        Object val = relValues.get(relName);
        if (val != null && val.equals("")) {
            val = queryRelationship(relName);
            relValues.put(relName, val);
        }

        return val;
    }
}
