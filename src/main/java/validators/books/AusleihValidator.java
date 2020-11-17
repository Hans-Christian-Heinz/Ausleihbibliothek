package validators.books;

import mappers.UserMapper;
import models.Book;
import models.User;
import validators.Validator;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class AusleihValidator extends Validator {
    public AusleihValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && validateExists("id", new Book());
        try {
            valid = valid && (validateEqual("user_id", "0") || validateSelf("user_id", new UserMapper()));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (valid) {
            errors = new HashMap<>();
        }

        return valid;
    }
}
