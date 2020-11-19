package validators.admin.users;

import help.MappersHelper;
import models.User;
import validators.Validator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ChangeRoleValidator extends Validator {
    public ChangeRoleValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateExists("id", MappersHelper.userMapper);
        try {
            valid = valid && validateNotSelf("id");
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
