package validators;

import java.sql.Connection;
import java.util.Map;

/**
 * Wenn für einen Controller kein Validator gefunden wird, schlägt die Post-Validierung fehl
 */
public class FalseValidator extends Validator {
    public FalseValidator(Map<String, String[]> params, Connection db) {
        super(params, db);
    }

    @Override
    public boolean validate() {
        return false;
    }
}
