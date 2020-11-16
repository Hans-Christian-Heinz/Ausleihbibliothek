package validators.admin.books;

import models.User;
import validators.Validator;

import java.sql.Connection;
import java.util.Map;

public class AdminBookValidator extends Validator {
    public AdminBookValidator(Map<String, String[]> params, Connection db, User currentUser) {
        super(params, db, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateRequired("author");
        valid = valid && validateRequired("name");

        return valid;
    }
}
