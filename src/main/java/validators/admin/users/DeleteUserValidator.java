package validators.admin.users;

import mappers.UserMapper;
import models.User;
import validators.Validator;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.Map;

public class DeleteUserValidator extends Validator {
    public DeleteUserValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && this.validateExists("id", new User());
        try {
            valid = valid && this.validateNotSelf("id", new UserMapper());
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
