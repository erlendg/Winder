package com.eim.winder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by Mari on 11.02.2016.
 * Repo class for AlertSettings. Handel the connection to AlertSettings table in database
 */
public class AlertSettingsRepo {
    public final static String TAG = "AlertSettingsRepo";
    private SQLiteDBHelper dbHelper;
    private Context context;
    private String table = SQLiteDBHelper.TABLE_ALERTSETTINGS;
    private String[] allColumns = { SQLiteDBHelper.A_ALERT_ID, SQLiteDBHelper.A_TEMPMIN, SQLiteDBHelper.A_TEMPMAX,
            SQLiteDBHelper.A_PRECIPITATIONMIN, SQLiteDBHelper.A_PRECIPITATIONMAX, SQLiteDBHelper.A_WINDSPEEDMIN,
            SQLiteDBHelper.A_WINDSPEEDMAX, SQLiteDBHelper.A_WINDIRECTION, SQLiteDBHelper.A_CHECKSUN, SQLiteDBHelper.A_CHECKINTERVAL,
            SQLiteDBHelper.A_MON, SQLiteDBHelper.A_TUE, SQLiteDBHelper.A_WED, SQLiteDBHelper.A_THU,
            SQLiteDBHelper.A_FRI, SQLiteDBHelper.A_SAT, SQLiteDBHelper.A_SUN, SQLiteDBHelper.A_ICON_NAME,
            SQLiteDBHelper.A_HASEVENTS, SQLiteDBHelper.A_LASTUPDATE, SQLiteDBHelper.A_LOC_ID};

    public AlertSettingsRepo(Context context) {
        this.context = context;
        dbHelper = new SQLiteDBHelper(context);
    }
    /**
     * For testing purposes only
     * @param context to initialize the database
     * @param sqLiteDBHelper database created
     */
    public AlertSettingsRepo(Context context, SQLiteDBHelper sqLiteDBHelper){
        this.context = context;
        dbHelper = sqLiteDBHelper;
    }

    /**
     * @return readable database
     */
    public SQLiteDatabase getReadDB(){
        return dbHelper.getReadableDatabase();
    }
    /**
     * @return writable database
     */

    private SQLiteDatabase getWriteDB(){
        return dbHelper.getWritableDatabase();
    }

    /**
     * Close database
     */
    public void close() {
        dbHelper.close();
        Log.i(TAG, "close()");
    }

    /**
     * Updates the field hasevents for the AlertSettings in database
     * @param id AlertSettings that needs to be updated
     * @param hasEvents true if it has weather events, false if it doesn't have weather events
     * @return true if successfully updated in database
     */
    public boolean updateAlertsettingsHasEvents(int id, int hasEvents){
        Log.i(TAG, "updateAlertsettingsHasEvents(" + id+ ")");
        SQLiteDatabase db = getWriteDB();
        ContentValues value = new ContentValues();
        value.put(SQLiteDBHelper.A_HASEVENTS, hasEvents);
        boolean ok = db.update(table, value, "_id="+id, null) > 0;
        close();
        return ok;
    }
    /**
     * Updates the field last for the AlertSettings database
     * @param id AlertSettings that needs to be updated
     * @param lastUpdate last time the weather was checked for the location
     * @return true if successfully updated in database
     */
    public boolean updateAlertsettingsNewLastUpdate(int id, String lastUpdate){
        Log.i(TAG, "updateAlertsettingsNewLastUpdate(" + id+ ")");
        SQLiteDatabase db = getWriteDB();
        ContentValues value = new ContentValues();
        value.put(SQLiteDBHelper.A_LASTUPDATE, lastUpdate);
        boolean ok = db.update(table, value, "_id="+id, null) > 0;
        close();
        return ok;
    }

    /**
     *  Deletes an alertsetting based on its id:
     * @param id of AlertSettings
     * @return true if successfully updated in database
     */
    public boolean deleteAlertSettings(int id){
        SQLiteDatabase db = getWriteDB();
        boolean ok = db.delete(table, SQLiteDBHelper.A_ALERT_ID + " = " + id, null) > 0;
        Log.i(TAG, "deleteAlertSettings: " + id + " "+ok);
        close();
        return ok;
    }

    /**
     * Inserts a new AlertSettings object in database
     * @param alert new object
     * @return id of the stored object
     */
    public long insertAlertSettings(AlertSettings alert){
        //Log.i(TAG, "insertAlertSettings()");
        SQLiteDatabase db = getWriteDB();
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.A_TEMPMIN, alert.getTempMin());
        values.put(SQLiteDBHelper.A_TEMPMAX, alert.getTempMax());
        values.put(SQLiteDBHelper.A_PRECIPITATIONMIN, alert.getPrecipitationMin());
        values.put(SQLiteDBHelper.A_PRECIPITATIONMAX, alert.getPrecipitationMax());
        values.put(SQLiteDBHelper.A_WINDSPEEDMIN, alert.getWindSpeedMin());
        values.put(SQLiteDBHelper.A_WINDSPEEDMAX, alert.getWindSpeedMax());
        values.put(SQLiteDBHelper.A_WINDIRECTION, alert.getWindDirection());
        values.put(SQLiteDBHelper.A_CHECKSUN, alert.isCheckSun());
        values.put(SQLiteDBHelper.A_CHECKINTERVAL, alert.getCheckInterval());
        values.put(SQLiteDBHelper.A_MON, alert.isMon());
        values.put(SQLiteDBHelper.A_TUE, alert.isTue());
        values.put(SQLiteDBHelper.A_WED, alert.isWed());
        values.put(SQLiteDBHelper.A_THU, alert.isThu());
        values.put(SQLiteDBHelper.A_FRI, alert.isFri());
        values.put(SQLiteDBHelper.A_SAT, alert.isSat());
        values.put(SQLiteDBHelper.A_SUN, alert.isSun());
        values.put(SQLiteDBHelper.A_ICON_NAME, alert.getIconName());
        values.put(SQLiteDBHelper.A_HASEVENTS, alert.hasEvents());
        values.put(SQLiteDBHelper.A_LASTUPDATE, alert.getLastUpdate());
        values.put(SQLiteDBHelper.A_LOC_ID, alert.getLocation().getId());
        long res = db.insert(table, null, values);
        close();
        return res;
    }

    /**
     * Update AlertSettings object in database based on id
     * @param alert object containing new values
     * @return number of rows updated (should be 1)
     */
    public long updateAlertSettings(AlertSettings alert){
        //Log.i(TAG, "insertAlertSettings()");
        SQLiteDatabase db = getWriteDB();
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.A_TEMPMIN, alert.getTempMin());
        values.put(SQLiteDBHelper.A_TEMPMAX, alert.getTempMax());
        values.put(SQLiteDBHelper.A_PRECIPITATIONMIN, alert.getPrecipitationMin());
        values.put(SQLiteDBHelper.A_PRECIPITATIONMAX, alert.getPrecipitationMax());
        values.put(SQLiteDBHelper.A_WINDSPEEDMIN, alert.getWindSpeedMin());
        values.put(SQLiteDBHelper.A_WINDSPEEDMAX, alert.getWindSpeedMax());
        values.put(SQLiteDBHelper.A_WINDIRECTION, alert.getWindDirection());
        values.put(SQLiteDBHelper.A_CHECKSUN, alert.isCheckSun());
        values.put(SQLiteDBHelper.A_CHECKINTERVAL, alert.getCheckInterval());
        values.put(SQLiteDBHelper.A_MON, alert.isMon());
        values.put(SQLiteDBHelper.A_TUE, alert.isTue());
        values.put(SQLiteDBHelper.A_WED, alert.isWed());
        values.put(SQLiteDBHelper.A_THU, alert.isThu());
        values.put(SQLiteDBHelper.A_FRI, alert.isFri());
        values.put(SQLiteDBHelper.A_SAT, alert.isSat());
        values.put(SQLiteDBHelper.A_SUN, alert.isSun());
        values.put(SQLiteDBHelper.A_ICON_NAME, alert.getIconName());
        values.put(SQLiteDBHelper.A_HASEVENTS, alert.hasEvents());
        values.put(SQLiteDBHelper.A_LASTUPDATE, alert.getLastUpdate());
        long res = db.update(table, values, "_id=" + alert.getId(), null);
        close();
        return res;
    }

    /**
     * @return an ArrayList of AlertSettings objects:
     */
    public ArrayList<AlertSettings> getAllAlertSettings(){
        SQLiteDatabase db = getReadDB();
        //Log.i(TAG, "getAllAlertSettings()");
        ArrayList<AlertSettings> alertsettings = new ArrayList<>();
        Cursor c = db.query(table, null, null, null, null, null, null, "10");
        c.moveToFirst();
        while(!c.isAfterLast()) {
            AlertSettings alert = cursorToAlertSettings(c);
            // add to list
            alertsettings.add(alert);
            c.moveToNext();
        }
        c.close();
        return alertsettings;
    }

    /**
     * Helping method to parse cursor values and store them inside an AlertSettings object
     * @param c cursor
     * @return AlertSettings object
     */
    private AlertSettings cursorToAlertSettings(Cursor c) {
        AlertSettings alert = new AlertSettings(c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_ALERT_ID)),
                c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_TEMPMIN)), c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_TEMPMAX)),
                c.getDouble(c.getColumnIndexOrThrow(SQLiteDBHelper.A_PRECIPITATIONMIN)), c.getDouble(c.getColumnIndexOrThrow(SQLiteDBHelper.A_PRECIPITATIONMAX)),
                c.getDouble(c.getColumnIndexOrThrow(SQLiteDBHelper.A_WINDSPEEDMIN)), c.getDouble(c.getColumnIndexOrThrow(SQLiteDBHelper.A_WINDSPEEDMAX)),
                c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.A_WINDIRECTION)),c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_CHECKSUN)),
                c.getDouble(c.getColumnIndexOrThrow(SQLiteDBHelper.A_CHECKINTERVAL)),c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_MON)),
                c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_TUE)),c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_WED)),
                c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_THU)), c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_FRI)),
                c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_SAT)), c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_SUN)),
                c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.A_ICON_NAME)), c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_HASEVENTS)), null, c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.A_LASTUPDATE)));
        Location location = new Location();
        location.setId(c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.A_LOC_ID)));
        alert.setLocation(location);
        return alert;
    }

    /**
     * @param id of AlertSettings
     * @return a AlertSettings object based on the id
     */
    public AlertSettings getAlertSettingById(int id){
        //Log.i(TAG, "getAlertSettingById("+ id+")");
        SQLiteDatabase db = getReadDB();
        Cursor c = db.query(table, null, SQLiteDBHelper.A_ALERT_ID+ "=?", new String[]{""+id}, null, null, null);
        //Cursor c = db.rawQuery("SELECT * FROM " + table + " WHERE _id = " + id,null);
        c.moveToFirst();
        AlertSettings res = cursorToAlertSettings(c);
        c.close();
        return res;
    }
}
