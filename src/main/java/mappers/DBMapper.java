package mappers;

import models.ARModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class DBMapper {
    private Class<? extends ARModel> modelClass;
    private String table;
    private Map<String, String> propertyMap;

    /**
     * Konstruktor sollte im parameterlosen Konstruktor der Subklassen aufgerufen werden
     *
     * @param modelClass
     * @param table
     * @param propertyMap
     */
    public DBMapper(Class<? extends ARModel> modelClass, String table, Map<String, String> propertyMap) {
        this.modelClass = modelClass;
        this.table = table;
        this.propertyMap = propertyMap;
    }

    /**
     * retrieve a model instance by key
     *
     * @param index
     * @param val
     * @param db
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public ARModel getByKey(String index, String val, Connection db) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ARModel model = modelClass.getConstructor().newInstance();

        Set<String> keySet = propertyMap.keySet();

        //Baue die Datenbankabfrage auf: Lese alle Spalten, die in der PropertyMap vorhanden sind aus.
        StringBuilder query = this.getSelectHelp();
        query.append(" WHERE ").append(index).append("='").append(val).append("';");

        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query.toString());
            //First, da bei id-Suche nur eine Zeile ausgelesen wird
            if (res.first()) {
                for (String key : keySet) {
                    //Benutze reflection, um Setter aufzurufen
                    Field feld = modelClass.getDeclaredField(key);
                    Method setter =
                            modelClass.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), feld.getType());
                    setter.invoke(model, res.getObject(propertyMap.get(key)));
                }
            }
        } catch(SQLException | NoSuchMethodException | NoSuchFieldException e) {
            //TODO
        }

        return model;
    }

    /**
     * Methode überschreibt das aktuelle Objekt mit den aus Daten aus der Datenbank
     *
     * @param id
     * @param db
     */
    public ARModel getById(long id, Connection db) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.getByKey("id", String.valueOf(id), db);
    }

    /*
     * Gebe eine Liste aller Datensätze aus; todo pagination
     *
     * @param db
     */
    /*public void getAll(Connection db) {
        StringBuilder query = this.getSelectHelp();
        query.append(";");

        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query.toString());
            //First, da bei id-Suche nur eine Zeile ausgelesen wird
            while (res.next()) {

            }
        } catch(SQLException e) {
            //TODO
        }
    }*/

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
