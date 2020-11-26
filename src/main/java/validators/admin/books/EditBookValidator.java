package validators.admin.books;

import exceptions.DBMapperException;
import help.MappersHelper;
import models.User;
import validators.Validator;

import java.util.Map;

public class EditBookValidator extends Validator {
    public EditBookValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() throws DBMapperException {
        boolean valid = true;

        String id = params.get("id")[0];

        valid = valid && validateRequired("id");
        validateExists("id", MappersHelper.bookMapper);
        valid = valid && validateRequired("author");
        valid = valid && validateRegex("author", "(?U)[\\p{L}\\p{M}\\s'-]+");
        valid = valid && validateRequired("name");
        valid = valid && validateRegex("author", "(?U)[\\p{L}\\p{M}\\s'-]+");

        if (! valid) {
            //Fehlermeldung wird verwendet, um das Formular (modales Fenster) unmittelbar anzuzeigen.
            errors.put("modal", "editBookModal" + id);
        }

        return valid;
    }
}
