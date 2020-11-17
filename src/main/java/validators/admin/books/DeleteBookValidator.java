package validators.admin.books;

import models.Book;
import models.User;
import validators.Validator;
import java.util.Map;

public class DeleteBookValidator extends Validator {
    public DeleteBookValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && this.validateExists("id", new Book());

        return valid;
    }
}
