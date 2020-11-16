package validators.auth;

import models.User;
import validators.Validator;

import java.sql.Connection;
import java.util.Map;

public class RegisterValidator extends Validator {
    public RegisterValidator(Map<String, String[]> params, Connection db, User currentUser) {
        super(params, db, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateRequired("username");
        valid = valid && validateRequired("name");
        valid = valid && validateRequired("vorname");
        valid = valid && validateRequired("password");
        valid = valid && validateRequired("password_repeat");
        valid = valid && validateUnique("username", new User(), null);
        valid = valid && validatePwd("password");
        valid = valid && validatePwd("password_repeat");
        valid = valid && validateSame("password", "password_repeat");

        return valid;
    }
}
