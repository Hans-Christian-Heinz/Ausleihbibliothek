package validators.auth;

import help.MappersHelper;
import models.User;
import validators.Validator;

import java.util.Map;

public class RegisterValidator extends Validator {
    public RegisterValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateRequired("username");
        valid = valid && validateRegex("username", "(?U)[\\p{L}\\p{M}\\s'-_.]+");
        valid = valid && validateRequired("name");
        valid = valid && validateRegex("name", "(?U)[\\p{L}\\p{M}\\s'-]+");
        valid = valid && validateRequired("vorname");
        valid = valid && validateRegex("vorname", "(?U)[\\p{L}\\p{M}\\s'-]+");
        valid = valid && validateRequired("password");
        valid = valid && validateRequired("password_repeat");
        valid = valid && validateUnique("username", MappersHelper.userMapper, null);
        valid = valid && validatePwd("password");
        valid = valid && validatePwd("password_repeat");
        valid = valid && validateSame("password", "password_repeat");

        return valid;
    }
}
