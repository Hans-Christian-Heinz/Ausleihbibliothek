package models.relationships;

import mappers.DBMapper;
import models.DBModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Connection;

public class BelongsTo extends Relationship {
    public BelongsTo(String name, DBModel owner, Class<? extends DBModel> otherClass, String ownFk) {
        this.name = name;
        this.owner = owner;
        this.otherClass = otherClass;
        this.ownFk = ownFk;
    }

    @Override
    public Object queryRelationship() {
        try {
            Method getMapper = otherClass.getDeclaredMethod("getMapper");
            DBMapper mapper = (DBMapper) getMapper.invoke(otherClass);
            Method getOwnFk = owner.getClass().getDeclaredMethod("get" + ownFk.substring(0, 1).toUpperCase() + ownFk.substring(1));
            BigInteger id = (BigInteger) getOwnFk.invoke(owner);

            if (id != null)
                return mapper.getById(id.longValue());
            else
                return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
