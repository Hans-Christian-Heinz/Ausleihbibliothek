package validators.profile;

import models.User;
import validators.Validator;

import java.sql.Connection;
import java.util.Map;

public class EditProfileValidator extends Validator {
    public EditProfileValidator(Map<String, String[]> params, Connection db) {
        super(params, db);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateRequired("username");
        valid = valid && validateRequired("name");
        valid = valid && validateRequired("vorname");
        valid = valid && validateUnique("username", new User(), false);

        if (! valid) {
            //Fehlermeldung wird verwendet, um das Formular (modales Fenster) unmittelbar anzuzeigen.
            errors.put("modal", "editProfileModal");
        }

        return valid;
    }
}
