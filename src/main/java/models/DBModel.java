package models;

import exceptions.DBMapperException;
import models.relationships.HasMany;
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

    private Object queryRelationship(String relName, int perPage, int currentPage) throws DBMapperException {
        Relationship rel = relationships.get(relName);
        if (rel == null) {
            return null;
        }

        if (rel instanceof HasMany) {
            return ((HasMany) rel).queryRelationship(this, perPage, currentPage);
        } else {
            return rel.queryRelationship(this);
        }
    }

    public Object getRelValue(String relName) throws DBMapperException {
        return getRelValue(relName, -1, -1);
    }

    public Object getRelValue(String relName, int perPage, int currentPage) throws DBMapperException {
        Object val = relValues.get(relName);
        if (val != null && val.equals("")) {
            val = queryRelationship(relName, perPage, currentPage);
            relValues.put(relName, val);
        }

        return val;
    }

    public int getRelCount(String relName) throws DBMapperException {
        if (relationships.containsKey(relName)) {
            Relationship rel = relationships.get(relName);
            if (rel instanceof HasMany) {
                return ((HasMany)rel).count(this);
            }
            else {
                Object val = getRelValue(relName);
                if (val == null || "".equals(val)) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
        }
        return 0;
    }
}
