package validators.auth;

import models.User;
import validators.Validator;

import java.util.Map;

public class LogoutValidator extends Validator {
    public LogoutValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        return true;
    }
}
