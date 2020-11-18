package validators.admin.books;

import models.User;
import validators.Validator;

import java.util.Map;

public class AdminBookValidator extends Validator {
    public AdminBookValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateRequired("author");
        valid = valid && validateRegex("author", "(?U)[\\p{L}\\p{M}\\s'-]+");
        valid = valid && validateRequired("name");
        valid = valid && validateRegex("author", "(?U)[\\p{L}\\p{M}\\s'-]+");

        return valid;
    }
}
