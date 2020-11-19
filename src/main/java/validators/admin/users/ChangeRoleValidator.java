package validators.admin.users;

import exceptions.DBMapperException;
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
    public boolean validate() throws DBMapperException {
        boolean valid = true;

        valid = valid && validateExists("id", MappersHelper.userMapper);
        valid = valid && validateNotSelf("id");

        return valid;
    }
}
