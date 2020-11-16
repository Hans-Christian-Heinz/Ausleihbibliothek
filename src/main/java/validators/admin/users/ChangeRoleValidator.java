package validators.admin.users;

import mappers.UserMapper;
import models.User;
import validators.Validator;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.Map;

public class ChangeRoleValidator extends Validator {
    public ChangeRoleValidator(Map<String, String[]> params, Connection db, User currentUser) {
        super(params, db, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateExists("id", new User());
        try {
            valid = valid && validateNotSelf("id", new UserMapper());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return valid;
    }
}
