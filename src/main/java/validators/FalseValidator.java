package validators;

import models.User;

import java.sql.Connection;
import java.util.Map;

/**
 * Wenn für einen Controller kein Validator gefunden wird, schlägt die Post-Validierung fehl
 */
public class FalseValidator extends Validator {
    public FalseValidator(Map<String, String[]> params, User currentUser) {
        super(params, currentUser);
    }

    @Override
    public boolean validate() {
        return false;
    }
}
