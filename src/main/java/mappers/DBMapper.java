package mappers;

import db.DatabaseHelper;
import exceptions.DBMapperException;
import models.DBModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public abstract class DBMapper {
    private final Class<? extends DBModel> modelClass;
    private final String table;
    private final Map<String, String> propertyMap;
    private final Connection db;

    /**
     * Konstruktor; ruft abstrakte Methoden auf
     */
    public DBMapper() {
        modelClass = stdClass();
        table = stdTable();
        propertyMap = stdPropertyMap();
        db = DatabaseHelper.getConnection();
    }

    /**
     * liefert die dem Mapper zugehörige Model-Klasse
     * @return
     */
    protected abstract Class<? extends DBModel> stdClass();

    /**
     * liefert den Namen der dem Mapper zugehörigen Datenbanktabelle
     * @return
     */
    protected abstract String stdTable();

    /**
     * Ordne den Feldern eines Objekts Spalten der zugehörigen Datenbanktabelle zu
     * Schlüssel: Feldname des Objekts, Wert: Spaltenname der Tabelle
     *
     * @return
     */
    protected abstract Map<String, String> stdPropertyMap();

    public Class<? extends DBModel> getModelClass() {
        return modelClass;
    }

    public String getTable() {
        return table;
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    /**
     * retrieve a model instance by key
     *
     * @param index
     * @param val
     * @return
     * @throws DBMapperException
     */
    public DBModel getByKey(String index, String val) throws DBMapperException {
        DBModel model;
        try {
            model = modelClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DBMapperException("Klasse " + modelClass.getCanonicalName() + " konnte nicht instanziiert werden.");
        }

        Set<String> keySet = propertyMap.keySet();

        //Baue die Datenbankabfrage auf: Lese alle Spalten, die in der PropertyMap vorhanden sind aus.
        StringBuilder query = this.getSelectHelp();
        query.append(" WHERE ").append(index).append("='").append(val).append("';");

        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query.toString());
            //First, da bei id-Suche nur eine Zeile ausgelesen wird
            //Eigentlich lieber res.first(), das funktioniert aber mit sqlite nicht. (Test-DB)
            if (res.next()) {
                for (String key : keySet) {
                    //Benutze reflection, um Setter aufzurufen
                    //Field feld = modelClass.getDeclaredField(key);
                    Object value = res.getObject(propertyMap.get(key));
                    if (value != null) {
                        //Method setter =
                        //      modelClass.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), feld.getType());
                        String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                        try {
                            Method setter = modelClass.getDeclaredMethod(methodName, value.getClass());
                            setter.invoke(model, value);
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            throw new DBMapperException("Methode " + methodName + " konnte auf einer Instanz von "
                                    + modelClass.getCanonicalName() + " nicht aufgerufen werden.");
                        }
                    }
                }
            }
            else {
                return null;
            }
        } catch(SQLException e) {
            throw new DBMapperException("Bei der folgenden Datenbankanfrage ist ein Fehler aufgetreten:\n" + query.toString());
        }

        return model;
    }

    /**
     * Methode überschreibt das aktuelle Objekt mit den aus Daten aus der Datenbank
     *
     * @param id
     */
    public DBModel getById(long id) throws DBMapperException {
        return this.getByKey("id", String.valueOf(id));
    }

    /**
     * Gebe eine Liste aller Datensätze aus;
     *
     * @return
     */
    public List<DBModel> getAll() throws DBMapperException {
        return getAllWhereIndex(null, null);
    }

    /**
     * returns a collection of (up to) perPage models to be displayed on the current page
     *
     * @param perPage number of models per page
     * @param currentPage starts with 0
     * @return list of items to be displayed
     */
    public List<DBModel> getPagination(int perPage, int currentPage) throws DBMapperException {
        if (perPage < 0 || currentPage < 0) {
            return getAll();
        }

        StringBuilder query = getSelectHelp();
        query.append(" LIMIT ").append(perPage).append(" OFFSET ").append(perPage * currentPage).append(";");

        return executeSelect(query.toString());
    }

    public List<DBModel> getPaginationWhereIndex(String index, String val, int perPage, int currentPage) throws DBMapperException {
        StringBuilder query = this.getSelectHelp();
        if (index != null) {
            query.append(" WHERE " + propertyMap.get(index) + "=");
            if (val == null) {
                query.append("NULL");
            }
            else {
                query.append("'").append(val).append("'");
            }
        }
        query.append(" LIMIT ").append(perPage).append(" OFFSET ").append(perPage * currentPage).append(";");

        return executeSelect(query.toString());
    }

    /**
     * Gebe die Anzahl an Datensätzen aus.
     *
     * @return
     */
    public int count() throws DBMapperException {
        String query = "SELECT COUNT(id) AS anzahl FROM " + table + ";";

        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query);
            //First, da bei id-Suche nur eine Zeile ausgelesen wird
            //Eigentlich lieber res.first(), das funktioniert aber mit sqlite nicht. (Test-DB)
            if (res.next()) {
                return res.getInt("anzahl");
            }
            else {
                return 0;
            }
        } catch(SQLException e) {
            throw new DBMapperException("Bei der folgenden Datenbankanfrage ist ein Fehler aufgetreten:\n" + query);
        }
    }

    public int countWhereIndex(String index, String val) throws DBMapperException {
        String query = "SELECT COUNT(id) AS anzahl FROM " + table + " WHERE " + propertyMap.get(index) + "=" + val + ";";

        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query);
            //First, da bei id-Suche nur eine Zeile ausgelesen wird
            //Eigentlich lieber res.first(), das funktioniert aber mit sqlite nicht. (Test-DB)
            if (res.next()) {
                return res.getInt("anzahl");
            }
            else {
                return 0;
            }
        } catch(SQLException e) {
            throw new DBMapperException("Bei der folgenden Datenbankanfrage ist ein Fehler aufgetreten:\n" + query);
        }
    }

    /**
     * Gebe eine Liste aller Datensätze aus, wobei ein Index vorgeschreiben wird
     *
     * @param index
     * @param val
     * @return
     */
    public List<DBModel> getAllWhereIndex(String index, String val) throws DBMapperException {
        StringBuilder query = this.getSelectHelp();
        if (index != null) {
            query.append(" WHERE " + propertyMap.get(index) + "=");
            if (val == null) {
                query.append("NULL");
            }
            else {
                query.append("'").append(val).append("'");
            }
        }
        query.append(";");

        return executeSelect(query.toString());
    }

    /**
     * Füge eine neue Zeile in die Datenbank ein
     *
     * @param model
     */
    public void insert(DBModel model) throws DBMapperException {
        if (! modelClass.isInstance(model)) {
            throw new DBMapperException("Beim Versuch, eine Instanz von " + model.getClass().getCanonicalName() +
                    " mithilfe einer Instanz von " + this.getClass().getCanonicalName() + " zu speichern, ist ein Fehler aufgetreten.");
        }
        Set<String> keySet = propertyMap.keySet();

        //Baue die Datenbankabfrage auf: Lese alle Spalten, die in der PropertyMap vorhanden sind aus.
        StringBuilder query = new StringBuilder("INSERT INTO ").append(table) .append(" (");
        Iterator<String> keyIt = keySet.iterator();
        StringBuilder values = new StringBuilder();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            if (! key.equals("id")) {
                String methodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
                try {
                    Method getter = modelClass.getDeclaredMethod(methodName);
                    Object value = getter.invoke(model);
                    query.append(propertyMap.get(key));
                    if (value != null) {
                        values.append("'").append(value.toString()).append("'");
                    }
                    else {
                        values.append("NULL");
                    }
                    if (keyIt.hasNext()) {
                        query.append(",");
                        values.append(", ");
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new DBMapperException("Methode " + methodName + " konnte auf einer Instanz von "
                            + modelClass.getCanonicalName() + " nicht aufgerufen werden.");
                }
            }
        }
        query.append(") VALUES (");
        query.append(values);
        query.append(");");

        try {
            Statement stmt = db.createStatement();
            //verschachteltes try catch: beim Testen wird sqlite verwendet und mit RETURN_GENERATED_KEYS wird eine Methode ausgelöst
            try {
                stmt.execute(query.toString(), Statement.RETURN_GENERATED_KEYS);
                ResultSet res = stmt.getGeneratedKeys();
                //First, nur eine Zeile eingefügt wurde
                if (res.first()) {
                    model.setId(BigInteger.valueOf(res.getLong("id")));
                }
            } catch(SQLException e) {
                stmt.execute(query.toString());
                //Suche die maximale id; (RETURN_GENERATED_KEYS für sqlite (TestDB) nicht implementiert
                String q = "SELECT MAX(id) as m FROM " + table + ";";
                Statement s = db.createStatement();
                ResultSet r = s.executeQuery(q);
                if (r.next()) {
                    model.setId(BigInteger.valueOf(r.getLong("m")));
                }
            }
        } catch (SQLException throwables) {
            throw new DBMapperException("Bei der folgenden Datenbankanfrage ist ein Fehler aufgetreten:\n" + query.toString());
        }
    }

    /**
     * Aktualisiere eine vorhandene Zeile in der Datenbank
     *
     * @param model
     */
    public void update(DBModel model) throws DBMapperException {
        Set<String> keySet = propertyMap.keySet();

        //Baue die Datenbankabfrage auf: Lese alle Spalten, die in der PropertyMap vorhanden sind aus.
        StringBuilder query = new StringBuilder("UPDATE ").append(table) .append(" SET ");
        Iterator<String> keyIt = keySet.iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            if (! key.equals("id")) {
                String methodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
                try {
                    Method getter = modelClass.getDeclaredMethod(methodName);
                    Object value = getter.invoke(model);
                    if (value == null) {
                        query.append(propertyMap.get(key)).append("=NULL");
                    }
                    else {
                        query.append(propertyMap.get(key)).append("='").append(value.toString()).append("'");
                    }
                    if (keyIt.hasNext()) {
                        query.append(", ");
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new DBMapperException("Methode " + methodName + " konnte auf einer Instanz von "
                            + modelClass.getCanonicalName() + " nicht aufgerufen werden.");
                }
            }
        }
        query.append(" WHERE id=").append(model.getId()).append(";");

        try {
            Statement stmt = db.createStatement();
            stmt.execute(query.toString());
        } catch(SQLException e) {
            throw new DBMapperException("Bei der folgenden Datenbankanfrage ist ein Fehler aufgetreten:\n" + query.toString());
        }
    }

    /**
     * Entferne das Modell aus der Datenbank
     *
     * @param id
     * @return
     */
    public void delete(BigInteger id) throws DBMapperException {
        String query = "DELETE FROM " + table + " WHERE id=" + id + ";";
        try {
            Statement stmt = db.createStatement();
            stmt.execute(query);
        } catch (SQLException throwables) {
            throw new DBMapperException("Bei der folgenden Datenbankanfrage ist ein Fehler aufgetreten:\n" + query);
        }
    }

    //Hilfsmethode, die den Beginn der SELECT query baut
    private StringBuilder getSelectHelp() {
        Collection<String> cols = propertyMap.values();

        //Baue die Datenbankabfrage auf: Lese alle Spalten, die in der PropertyMap vorhanden sind aus.
        StringBuilder query = new StringBuilder("SELECT");
        Iterator<String> colIt = cols.iterator();
        while (colIt.hasNext()) {
            query.append(" ").append(colIt.next());
            if (colIt.hasNext()) {
                query.append(",");
            }
        }
        query.append(" FROM ").append(table);

        return query;
    }

    private List<DBModel> executeSelect(String query) throws DBMapperException {
        List<DBModel> erg = new ArrayList<>();
        Set<String> keySet = propertyMap.keySet();

        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query.toString());
            //First, da bei id-Suche nur eine Zeile ausgelesen wird
            while (res.next()) {
                DBModel model;
                try {
                    model = modelClass.getConstructor().newInstance();
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                    throw new DBMapperException("Klasse " + modelClass.getCanonicalName() + " konnte nicht instanziiert werden.");
                }
                for (String key : keySet) {
                    //Benutze reflection, um Setter aufzurufen
                    //Field feld = modelClass.getDeclaredField(key);
                    Object value = res.getObject(propertyMap.get(key));
                    if (value != null) {
                        //Method setter =
                        //      modelClass.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), feld.getType());
                        String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                        try {
                            Method setter =
                                    modelClass.getDeclaredMethod(methodName, value.getClass());
                            setter.invoke(model, value);
                        }
                        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            throw new DBMapperException("Methode " + methodName + " konnte auf einer Instanz von "
                                    + modelClass.getCanonicalName() + " nicht aufgerufen werden.");
                        }
                    }
                }

                erg.add(model);
            }
        } catch(SQLException e) {
            throw new DBMapperException("Bei der folgenden Datenbankanfrage ist ein Fehler aufgetreten:\n" + query);
        }

        return erg;
    }
}
