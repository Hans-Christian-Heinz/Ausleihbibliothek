package validators.auth;

import models.User;
import validators.Validator;

import java.sql.Connection;
import java.util.Map;

public class LoginValidator extends Validator {
    public LoginValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateRequired("username");
        valid = valid && validateRequired("password");
        valid = valid && validateExists("username", new User());

        return valid;
    }
}
