package validators.admin.users;

import models.User;
import validators.Validator;

import java.lang.reflect.InvocationTargetException;
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
            valid = valid && this.validateNotSelf("id");
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
