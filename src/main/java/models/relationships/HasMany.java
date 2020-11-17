package models.relationships;

import mappers.DBMapper;
import models.DBModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

public class HasMany extends Relationship {
    public HasMany(Class<? extends DBModel> ownerClass, Class<? extends DBModel> otherClass, String otherFk) {
        this.ownerClass = ownerClass;
        this.otherClass = otherClass;
        this.otherFk = otherFk;
    }

    @Override
    public Object queryRelationship(DBModel owner) {
        if (! ownerClass.isInstance(owner)) {
            //todo exception
        }
        //first: get the id of the owner
        BigInteger id = owner.getId();
        try {
            Method getMapper = otherClass.getDeclaredMethod("getMapper");
            DBMapper mapper = (DBMapper) getMapper.invoke(otherClass);
            return mapper.getAllWhereIndex(otherFk, id.toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*@Override
    public String getSqlJoin() {
        try {
            Method getMapper = ownerClass.getDeclaredMethod("getMapper");
            DBMapper mapper = (DBMapper) getMapper.invoke(ownerClass);
            Method getOtherMapper = otherClass.getDeclaredMethod("getMapper");
            DBMapper otherMapper = (DBMapper) getOtherMapper.invoke(otherClass);
            StringBuilder join = new StringBuilder(" LEFT JOIN ");
            join.append(otherMapper.getTable()).append(" ON ").append(mapper.getTable()).append(".id=");
            join.append(otherMapper.getTable()).append(".").append(otherFk);

            return join.toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            //todo
            return "";
        }
    }*/
}
