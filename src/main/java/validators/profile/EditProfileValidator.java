package validators.profile;

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
        valid = valid && validateUnique("username", "users", false);

        if (! valid) {
            //Fehlermeldung wird verwendet, um hoffentlich das Formular (modales Fenster) unmittelbar anzuzeigen.
            errors.put("editProfile", "Beim Bearbeiten des Profils sind Fehler aufgetreten");
        }

        return valid;
    }
}
