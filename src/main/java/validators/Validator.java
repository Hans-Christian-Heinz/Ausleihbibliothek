package validators;

import db.DatabaseHelper;
import exceptions.DBMapperException;
import help.MappersHelper;
import mappers.DBMapper;
import mappers.UserMapper;
import models.DBModel;
import models.User;

import java.lang.reflect.InvocationTargetException;
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
    private final Connection db;
    private User currentUser;

    public Validator(Map<String, String[]> params, User currentUser) {
        this.params = params;
        errors = new HashMap<>();
        this.db = DatabaseHelper.getConnection();
        this.currentUser = currentUser;
    }

    public abstract boolean validate() throws DBMapperException;

    protected boolean validateRegex(String key, String pattern) {
        String val = params.get(key)[0];
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(val);

        if (! m.matches()) {
            errors.put(key, "Das Feld " + key + " muss dem regulären Ausdruck " + pattern + " entsprechen.");
            return false;
        }
        return true;
    }

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
     * Stelle siche, dass der Wert für key mit dem übergebenen Objekt übereinstimmt.
     * @param key
     * @param val
     * @return
     */
    protected boolean validateEqual(String key, String val) {
        String v = params.get(key)[0];
        if (v.equals(val)) {
            return true;
        }
        else {
            errors.put(key, "Der Wert für " + key + " muss " + val + " sein.");
            return false;
        }
    }

    /**
     * Überprüfe, ob der Wert für key in der Datenbank bereits vorliegt
     *
     * @param key
     * @param mapper
     * @param id
     * @return
     */
    protected boolean validateUnique(String key, DBMapper mapper, String id) {
        String table = mapper.getTable();
        String dbKey = mapper.getPropertyMap().get(key);

        try{
            String val = params.get(key)[0];
            String query = "SELECT COUNT(id) AS anzahl FROM " + table + " WHERE " + dbKey + "='" + val + "';";
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query);
            //First, da nur eine Zeile ausgelesen wird
            if (res.first()) {
                //Benutze reflection, um Setter aufzurufen
                int count = res.getInt("anzahl");
                if (id == null) {
                    if (count != 0) {
                        errors.put(key, "Der eingegebene Wert für " + key + " ist in der Datenbank bereits vorhanden.");
                        return false;
                    }
                }
                else {
                    //Überprüfen, ob der Wert für ein anderes Profil vorliegt
                    if (count == 1) {
                        String q2 = "SELECT " + dbKey + " FROM " + table + " WHERE id=" + id + ";";
                        Statement s2 = db.createStatement();
                        ResultSet r2 = s2.executeQuery(q2);
                        if (r2.first()) {
                            if (val.equals(r2.getObject(dbKey))) {
                                return true;
                            }
                            else {
                                errors.put(key, "Der eingegebene Wert für " + key + " ist in der Datenbank bereits für einen anderen Datensatz vorhanden.");
                                return false;
                            }
                        }
                    }
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
     * @param mapper
     * @return
     */
    protected boolean validateExists(String key, DBMapper mapper) throws DBMapperException {
        String table = mapper.getTable();
        String dbKey = mapper.getPropertyMap().get(key);
        String val = params.get(key)[0];
        String query = "SELECT COUNT(id) AS anzahl FROM " + table + " WHERE " + dbKey + "='" + val + "';";

        try{
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
            throw new DBMapperException("Bei der folgenden Datenbankanfrage ist ein Fehler aufgetreten:\n" +
                    query);
            //errors.put(key, "Das Vorkommen des Wertes in der Datenbank konnte nicht validiert werden.");
            //return false;
        }
    }

    /**
     * Stelle sicher, dass die übergebene id die des angemeldeten Benutzers ist
     *
     * @param key
     * @return
     * @throws DBMapperException
     */
    protected boolean validateSelf(String key) throws DBMapperException {
        UserMapper mapper = MappersHelper.userMapper;
        long id = Long.parseLong(params.get(key)[0]);
        DBModel user = mapper.getById(id);
        if (! currentUser.equals(user)) {
            errors.put(key, "Sie dürfen die ausgewählte Operation nur für Ihr eigenes Benutzerprofil durchführen.");
            return false;
        }
        return true;
    }

    /**
     * Stelle sicher, dass die übergebene id die des angemeldeten Benutzers ist
     *
     * @param key
     * @return
     * @throws DBMapperException
     */
    protected boolean validateNotSelf(String key) throws DBMapperException {
        UserMapper mapper = MappersHelper.userMapper;
        long id = Long.parseLong(params.get(key)[0]);
        DBModel user = mapper.getById(id);
        if (currentUser.equals(user)) {
            errors.put(key, "Sie dürfen die ausgewählte Operation nicht für Ihr eigenes Benutzerprofil durchführen.");
            return false;
        }
        return true;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
