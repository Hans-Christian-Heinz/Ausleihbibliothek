package validators.admin.users;

import exceptions.DBMapperException;
import help.MappersHelper;
import models.User;
import validators.Validator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DeleteUserValidator extends Validator {
    public DeleteUserValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() throws DBMapperException {
        boolean valid = true;

        valid = valid && this.validateExists("id", MappersHelper.userMapper);
        valid = valid && this.validateNotSelf("id");

        return valid;
    }
}
