package validators.profile;

import help.MappersHelper;
import models.User;
import validators.Validator;

import java.util.Map;

public class EditProfileValidator extends Validator {
    public EditProfileValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        String id = params.get("user_id")[0];

        valid = valid && validateRequired("username");
        valid = valid && validateRegex("username", "(?U)[\\p{L}\\p{M}\\s'-_.]+");
        valid = valid && validateRequired("name");
        valid = valid && validateRegex("name", "(?U)[\\p{L}\\p{M}\\s'-]+");
        valid = valid && validateRequired("vorname");
        valid = valid && validateRegex("vorname", "(?U)[\\p{L}\\p{M}\\s'-]+");
        valid = valid && validateUnique("username", MappersHelper.userMapper, id);

        if (! valid) {
            //Fehlermeldung wird verwendet, um das Formular (modales Fenster) unmittelbar anzuzeigen.
            errors.put("modal", "editProfileModal");
        }

        return valid;
    }
}
