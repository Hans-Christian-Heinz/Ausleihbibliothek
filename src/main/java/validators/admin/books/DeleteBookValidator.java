package validators.admin.books;

import exceptions.DBMapperException;
import help.MappersHelper;
import models.Book;
import models.User;
import validators.Validator;
import java.util.Map;

public class DeleteBookValidator extends Validator {
    public DeleteBookValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() throws DBMapperException {
        boolean valid = true;

        valid = valid && this.validateExists("id", MappersHelper.bookMapper);

        return valid;
    }
}
