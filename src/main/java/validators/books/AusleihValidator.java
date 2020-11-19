package validators.books;

import exceptions.DBMapperException;
import help.MappersHelper;
import models.Book;
import models.User;
import validators.Validator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class AusleihValidator extends Validator {
    public AusleihValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() throws DBMapperException {
        boolean valid = true;

        valid = valid && validateExists("id", MappersHelper.bookMapper);
        valid = valid && (validateEqual("user_id", "0") || validateSelf("user_id"));

        if (valid) {
            errors = new HashMap<>();
        }

        return valid;
    }
}
