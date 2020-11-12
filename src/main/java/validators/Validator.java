package validators;

import models.ARModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Validator {
    protected Map<String, String> errors;
    protected Map<String, String[]> params;
    private Connection db;

    public Validator(Map<String, String[]> params, Connection db) {
        this.params = params;
        errors = new HashMap<>();
        this.db = db;
    }

    public abstract boolean validate();

    protected boolean validateRequired(String key) {
        if (! params.containsKey(key) || params.get(key).length == 0 || params.get(key)[0] == null || params.get(key)[0].isEmpty()) {
            errors.put(key, "Das Feld " + key + " muss vorhanden sein.");
            return false;
        }
        return true;
    }

    /**
     * Stimmt der übermittelte Wert dem vorgegebenen Format eines Passworts überein?
     * Format: mindestens 8 Zeichen, mindestens 1 Großbuchstabe, 1 Kleinbuchstabe, 1 Ziffer
     *
     * @param key
     * @return
     */
    protected boolean validatePwd(String key) {
        Pattern pwd = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        Matcher m = pwd.matcher(params.get(key)[0]);
        if (! m.matches()) {
            errors.put(key, "Ein Passwort muss mindestens 8 Zeichen lang sein und mindestens 1 Großbuchstaben, einen Kleinbuchstaben und eine Ziffer beinhalten.");
            return false;
        }

        return true;
    }

    /**
     * Stelle sicher, dass die Werte für key1 und key2 übereinstimmen
     *
     * @param key1
     * @param key2
     * @return
     */
    protected boolean validateSame(String key1, String key2) {
        if (! params.get(key1)[0].equals(params.get(key2)[0])) {
            errors.put(key2, key2 + " muss mit " + key1 + " übereinstimmen.");
            return false;
        }

        return true;
    }

    /**
     * Überprüfe, ob der Wert für key in der Datenbank bereits vorliegt
     *
     * @param key
     * @param table
     * @param insert
     * @return
     */
    protected boolean validateUnique(String key, String table, boolean insert) {
        try{
            String val = params.get(key)[0];
            String query = "SELECT COUNT(id) AS anzahl FROM " + table + " WHERE " + key + "='" + val + "';";
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query);
            //First, da nur eine Zeile ausgelesen wird
            if (res.first()) {
                //Benutze reflection, um Setter aufzurufen
                int count = res.getInt("anzahl");
                if (insert) {
                    if (count != 0) {
                        errors.put(key, "Der eingegebene Wert für " + key + " ist in der Datenbank bereits vorhanden.");
                        return false;
                    }
                }
                else {
                    if (count > 1) {
                        errors.put(key, "Der eingegebene Wert für " + key + " ist in der Datenbank bereits für einen anderen Datensatz vorhanden.");
                        return false;
                    }
                }

                return true;
            }
            else {
                errors.put(key, "Die Einzigartigkeit des Wertes in der Datenbank konnte nicht validiert werden.");
                return false;
            }
        } catch(SQLException e) {
            errors.put(key, "Die Einzigartigkeit des Wertes in der Datenbank konnte nicht validiert werden.");
            return false;
        }
    }

    /**
     * Stelle sicher, dass ein übergebner Wert in der Datenbank existiert.
     *
     * @param key
     * @param table
     * @return
     */
    protected boolean validateExists(String key, String table) {
        try{
            String val = params.get(key)[0];
            String query = "SELECT COUNT(id) AS anzahl FROM " + table + " WHERE " + key + "='" + val + "';";
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query);
            //First, da nur eine Zeile ausgelesen wird
            if (res.first()) {
                //Benutze reflection, um Setter aufzurufen
                int count = res.getInt("anzahl");
                if (count == 0) {
                    errors.put(key, "Der für " + key + " angegebene Wert existiert in der Datenbank nicht.");
                    return false;
                }

                return true;
            }
            else {
                errors.put(key, "Das Vorkommen des Wertes in der Datenbank konnte nicht validiert werden.");
                return false;
            }
        } catch(SQLException e) {
            errors.put(key, "Das Vorkommen des Wertes in der Datenbank konnte nicht validiert werden.");
            return false;
        }
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
