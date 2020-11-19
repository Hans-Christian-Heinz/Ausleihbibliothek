package validators.profile;

import exceptions.DBMapperException;
import models.User;
import validators.Validator;

import java.util.Map;

public class DeleteSelfValidator extends Validator {
    public DeleteSelfValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() throws DBMapperException {
        return true;
    }
}
