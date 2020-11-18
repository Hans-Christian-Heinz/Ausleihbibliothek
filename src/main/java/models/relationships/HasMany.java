package models.relationships;

import mappers.DBMapper;
import models.DBModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.List;

public class HasMany extends Relationship {
    public HasMany(Class<? extends DBModel> ownerClass, Class<? extends DBModel> otherClass, String otherFk) {
        this.ownerClass = ownerClass;
        this.otherClass = otherClass;
        this.otherFk = otherFk;
    }

    @Override
    public Object queryRelationship(DBModel owner) {
        return queryRelationship(owner, -1, -1);
    }

    public List<DBModel> queryRelationship(DBModel owner, int perPage, int currentPage) {
        if (! ownerClass.isInstance(owner)) {
            //todo exception
        }
        //first: get the id of the owner
        BigInteger id = owner.getId();
        try {
            Method getMapper = otherClass.getDeclaredMethod("getMapper");
            DBMapper mapper = (DBMapper) getMapper.invoke(otherClass);
            if (perPage <= 0 || currentPage < 0) {
                return mapper.getAllWhereIndex(otherFk, id.toString());
            }
            else {
                return mapper.getPaginationWhereIndex(otherFk, id.toString(), perPage, currentPage);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int count(DBModel owner) {
        try {
            Method getMapper = otherClass.getDeclaredMethod("getMapper");
            DBMapper mapper = (DBMapper) getMapper.invoke(otherClass);

            return mapper.countWhereIndex(otherFk, owner.getId().toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
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
