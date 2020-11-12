package models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class ARModel implements Serializable {
    /**
     * Ordne den Feldern eines Objekts Spalten der zugehörigen Datenbanktabelle zu
     * Schlüssel: Feldname des Objekts, Wert: Spaltenname der Tabelle
     *
     * @return
     */
    protected abstract Map<String, String> getPropertyMap();

    /**
     * Gebe den Namen der zugehörigen Datenbanktabelle aus
     *
     * @return
     */
    protected abstract String getTable();

    public abstract void setId(BigInteger id);

    public void getByKey(String index, String val, Connection db) {
        Class<? extends ARModel> klasse = this.getClass();
        Map<String, String> propertyMap = this.getPropertyMap();
        Set<String> keySet = propertyMap.keySet();
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
        query.append(" FROM ").append(this.getTable());
        query.append(" WHERE ").append(index).append("='").append(val).append("';");

        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query.toString());
            //First, da bei id-Suche nur eine Zeile ausgelesen wird
            if (res.first()) {
                for (String key : keySet) {
                    //Benutze reflection, um Setter aufzurufen
                    Field feld = klasse.getDeclaredField(key);
                    Method setter =
                            klasse.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), feld.getType());
                    setter.invoke(this, res.getObject(propertyMap.get(key)));
                }
            }
        } catch(SQLException | NoSuchMethodException | NoSuchFieldException e) {
            //TODO
        } catch (IllegalAccessException | InvocationTargetException e) {
            //TODO
        }
    }

    /**
     * Methode überschreibt das aktuelle Objekt mit den aus Daten aus der Datenbank
     *
     * @param id
     * @param db
     */
    public void getById(long id, Connection db) {
        this.getByKey("id", String.valueOf(id), db);
    }

    /**
     * Füge eine neue Zeile in die Datenbank ein
     *
     * @param db
     */
    public boolean insert(Connection db) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends ARModel> klasse = this.getClass();
        Map<String, String> propertyMap = this.getPropertyMap();
        Set<String> keySet = propertyMap.keySet();
        Collection<String> cols = propertyMap.values();
        boolean erg = false;

        //Baue die Datenbankabfrage auf: Lese alle Spalten, die in der PropertyMap vorhanden sind aus.
        StringBuilder query = new StringBuilder("INSERT INTO ").append(this.getTable()) .append(" (");
        Iterator<String> keyIt = keySet.iterator();
        StringBuilder values = new StringBuilder();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            if (! key.equals("id")) {
                Method getter =
                        klasse.getDeclaredMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
                Object value = getter.invoke(this);
                if (value != null) {
                    query.append(propertyMap.get(key));
                    values.append("'").append(value.toString()).append("'");
                    if (keyIt.hasNext()) {
                        query.append(",");
                        values.append(", ");
                    }
                }
            }
        }
        query.append(") VALUES (");
        query.append(values);
        query.append(");");

        try {
            Statement stmt = db.createStatement();
            erg = stmt.execute(query.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet res = stmt.getGeneratedKeys();
            //First, nur eine Zeile eingefügt wurde
            if (res.first()) {
                this.setId(BigInteger.valueOf(res.getLong("id")));
            }
            else {
                if (this instanceof User) {
                    this.setId(BigInteger.valueOf(-1));
                }
            }
        } catch(SQLException e) {
            //TODO
        }

        return erg;
    }
}
