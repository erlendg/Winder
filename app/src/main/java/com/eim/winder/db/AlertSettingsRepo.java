package com.eim.winder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by Mari on 11.02.2016.
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

    public AlertSettingsRepo(Context context, SQLiteDBHelper sqLiteDBHelper){
        this.context = context;
        dbHelper = sqLiteDBHelper;
    }
    public SQLiteDatabase getReadDB(){
        //Log.i(TAG, "getReadDB()");
        return dbHelper.getReadableDatabase();
    }

    private SQLiteDatabase getWriteDB(){
        //Log.i(TAG, "getWriteDB()");
        return dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        Log.i(TAG, "close()");
    }
    // Check if an alert already is added for the location:
    public boolean alertAlreadyExist(Location location){
        boolean exist = false;
        SQLiteDatabase db = getReadDB();
        Cursor c = db.rawQuery("SELECT " + SQLiteDBHelper.A_ALERT_ID + " FROM " + table + " WHERE " + SQLiteDBHelper.A_ALERT_ID + " = '" + location.getId() + "'", null);
        if(c != null) {
            if(c.getCount()>0) {
                exist = true;
            }
        }
        c.close();
        close();
        return exist;
    }
    public boolean updateAlertsettingsHasEvents(int id, int hasEvents){
        Log.i(TAG, "updateAlertsettingsHasEvents(" + id+ ")");
        SQLiteDatabase db = getWriteDB();
        ContentValues value = new ContentValues();
        value.put(SQLiteDBHelper.A_HASEVENTS, hasEvents);
        boolean ok = db.update(table, value, "_id="+id, null) > 0;
        close();
        return ok;
    }
    public boolean updateAlertsettingsNewLastUpdate(int id, String lastUpdate){
        Log.i(TAG, "updateAlertsettingsNewLastUpdate(" + id+ ")");
        SQLiteDatabase db = getWriteDB();
        ContentValues value = new ContentValues();
        value.put(SQLiteDBHelper.A_LASTUPDATE, lastUpdate);
        boolean ok = db.update(table, value, "_id="+id, null) > 0;
        close();
        return ok;
    }
    // Deletes an alertsetting based on its id:
    public boolean deleteAlertSettings(int id){
        SQLiteDatabase db = getWriteDB();
        boolean ok = db.delete(table, SQLiteDBHelper.A_ALERT_ID + " = " + id, null) > 0;
        Log.i(TAG, "deleteAlertSettings: " + id + " "+ok);
        close();
        return ok;
    }
    //Lagre varselinstillinger i database:
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
    public ArrayList<AlertSettings> getAllAlertSettingsTest(){
        //Log.i(TAG, "getAllAlertSettings()");
        ArrayList<AlertSettings> alertsettings = new ArrayList<>();
        SQLiteDatabase db = getReadDB();
        Cursor res = db.rawQuery("select * from " + table + " limit 10", null);
        res.moveToFirst();
        while(!res.isAfterLast()) {
            AlertSettings alert = cursorToAlertSettings(res);
            // add to list
            alertsettings.add(alert);
            res.moveToNext();
        }
        res.close();
        close();
        return alertsettings;
    }
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
