package validators.auth;

import exceptions.DBMapperException;
import help.MappersHelper;
import models.User;
import validators.Validator;

import java.util.Map;

public class LoginValidator extends Validator {
    public LoginValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() throws DBMapperException {
        boolean valid = true;

        valid = valid && validateRequired("username");
        valid = valid && validateRegex("username", "(?U)[\\p{L}\\p{M}\\s'-_.]+");
        valid = valid && validateRequired("password");
        valid = valid && validatePwd("password");
        valid = valid && validateExists("username", MappersHelper.userMapper);

        return valid;
    }
}
