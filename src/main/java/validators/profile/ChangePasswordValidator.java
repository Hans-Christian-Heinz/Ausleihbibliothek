package validators.profile;

import models.User;
import validators.Validator;

import java.sql.Connection;
import java.util.Map;

public class ChangePasswordValidator extends Validator {
    public ChangePasswordValidator(Map<String, String[]> params, Connection db) {
        super(params, db);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateRequired("password");
        valid = valid && validateRequired("password_old");
        valid = valid && validateRequired("password_repeat");
        valid = valid && validatePwd("password");
        valid = valid && validatePwd("password_repeat");
        valid = valid && validateSame("password", "password_repeat");

        if (! valid) {
            //Fehlermeldung wird verwendet, um das Formular (modales Fenster) unmittelbar anzuzeigen.
            errors.put("modal", "changePwdModal");
        }

        return valid;
    }
}
