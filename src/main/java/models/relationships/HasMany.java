package models.relationships;

import mappers.DBMapper;
import models.DBModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

public class HasMany extends Relationship {
    public HasMany(String name, Class<? extends DBModel> ownerClass, Class<? extends DBModel> otherClass, String otherFk) {
        this.name = name;
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
}
