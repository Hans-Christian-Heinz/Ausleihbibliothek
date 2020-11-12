package validators.auth;

import validators.Validator;

import java.sql.Connection;
import java.util.Map;

public class RegisterValidator extends Validator {
    public RegisterValidator(Map<String, String[]> params, Connection db) {
        super(params, db);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateRequired("username");
        valid = valid && validateRequired("name");
        valid = valid && validateRequired("vorname");
        valid = valid && validateRequired("password");
        valid = valid && validateRequired("password_repeat");
        valid = valid && validateUnique("username", "users", true);
        valid = valid && validatePwd("password");
        valid = valid && validatePwd("password_repeat");
        valid = valid && validateSame("password", "password_repeat");

        return valid;
    }
}
