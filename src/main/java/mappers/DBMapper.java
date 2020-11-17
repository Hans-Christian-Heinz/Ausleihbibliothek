package mappers;

import db.DatabaseHelper;
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
    private Class<? extends DBModel> modelClass;
    private String table;
    private Map<String, String> propertyMap;
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
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public DBModel getByKey(String index, String val) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        DBModel model = modelClass.getConstructor().newInstance();

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
                        Method setter =
                                modelClass.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), value.getClass());
                        setter.invoke(model, value);
                    }
                }
            }
            else {
                return null;
            }
        } catch(SQLException | NoSuchMethodException e) {
            //TODO
            e.printStackTrace();
        }

        return model;
    }

    /**
     * Methode überschreibt das aktuelle Objekt mit den aus Daten aus der Datenbank
     *
     * @param id
     */
    public DBModel getById(long id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.getByKey("id", String.valueOf(id));
    }

    /**
     * Gebe eine Liste aller Datensätze aus; todo pagination
     *
     * @return
     */
    public List<DBModel> getAll() {
        return getAllWhereIndex(null, null);
    }

    /**
     * Gebe eine Liste aller Datensätze aus, wobei ein Index vorgeschreiben wird
     *
     * @param index
     * @param val
     * @return
     */
    public List<DBModel> getAllWhereIndex(String index, String val) {
        List<DBModel> erg = new ArrayList<>();
        Set<String> keySet = propertyMap.keySet();
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

        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query.toString());
            //First, da bei id-Suche nur eine Zeile ausgelesen wird
            while (res.next()) {
                DBModel model = modelClass.getConstructor().newInstance();
                for (String key : keySet) {
                    //Benutze reflection, um Setter aufzurufen
                    //Field feld = modelClass.getDeclaredField(key);
                    Object value = res.getObject(propertyMap.get(key));
                    if (value != null) {
                        //Method setter =
                        //      modelClass.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), feld.getType());
                        Method setter =
                                modelClass.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), value.getClass());
                        setter.invoke(model, value);
                    }
                }

                erg.add(model);
            }
        } catch(SQLException | NoSuchMethodException e) {
            //TODO
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return erg;
    }

    /**
     * Füge eine neue Zeile in die Datenbank ein
     *
     * @param model
     */
    public void insert(DBModel model) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, SQLException {
        if (! modelClass.isInstance(model)) {
            //TODO exception
        }
        Set<String> keySet = propertyMap.keySet();

        //Baue die Datenbankabfrage auf: Lese alle Spalten, die in der PropertyMap vorhanden sind aus.
        StringBuilder query = new StringBuilder("INSERT INTO ").append(table) .append(" (");
        Iterator<String> keyIt = keySet.iterator();
        StringBuilder values = new StringBuilder();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            if (! key.equals("id")) {
                Method getter =
                        modelClass.getDeclaredMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
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
            }
        }
        query.append(") VALUES (");
        query.append(values);
        query.append(");");

        Statement stmt = db.createStatement();
        try {
            stmt.execute(query.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet res = stmt.getGeneratedKeys();
            //First, nur eine Zeile eingefügt wurde
            if (res.first()) {
                model.setId(BigInteger.valueOf(res.getLong("id")));
            }
            else {

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
    }

    /**
     * Aktualisiere eine vorhandene Zeile in der Datenbank
     *
     * @param model
     */
    public void update(DBModel model) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Set<String> keySet = propertyMap.keySet();

        //Baue die Datenbankabfrage auf: Lese alle Spalten, die in der PropertyMap vorhanden sind aus.
        StringBuilder query = new StringBuilder("UPDATE ").append(table) .append(" SET ");
        Iterator<String> keyIt = keySet.iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            if (! key.equals("id")) {
                Method getter =
                        modelClass.getDeclaredMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
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
            }
        }
        query.append(" WHERE id=").append(model.getId()).append(";");

        try {
            Statement stmt = db.createStatement();
            stmt.execute(query.toString());
        } catch(SQLException e) {
            //TODO
        }
    }

    /**
     * Entferne das Modell aus der Datenbank
     *
     * @param id
     * @return
     */
    public void delete(BigInteger id) {
        String query = "DELETE FROM " + table + " WHERE id=" + id + ";";
        try {
            Statement stmt = db.createStatement();
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
}
