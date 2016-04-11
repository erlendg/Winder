package com.eim.winder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mari on 11.02.2016.
 */
public class AlertSettingsDSService {
    public final static String TAG = "AlertSettingsDSService";
    private SQLiteDatabase database;
    private SQLiteDBHelper dbHelper;
    private String table = SQLiteDBHelper.TABLE_ALERTSETTINGS;
    private String[] allColumns = { SQLiteDBHelper.C_ALERT_ID, SQLiteDBHelper.C_TEMPMIN, SQLiteDBHelper.C_TEMPMAX,
            SQLiteDBHelper.C_PRECIPITATIONMIN, SQLiteDBHelper.C_PRECIPITATIONMAX, SQLiteDBHelper.C_WINDSPEEDMIN,
            SQLiteDBHelper.C_WINDSPEEDMAX, SQLiteDBHelper.C_WINDIRECTION, SQLiteDBHelper.C_CHECKSUN, SQLiteDBHelper.C_CHECKINTERVAL,
            SQLiteDBHelper.C_MON, SQLiteDBHelper.C_TUE, SQLiteDBHelper.C_WED, SQLiteDBHelper.C_THU,
            SQLiteDBHelper.C_FRI, SQLiteDBHelper.C_SAT, SQLiteDBHelper.C_SUN};

    public AlertSettingsDSService( Context context) {
        this.dbHelper = new SQLiteDBHelper(context);
    }
    public void open()throws SQLException {
        Log.i(TAG, "open()");
        database = dbHelper.getWritableDatabase();

    }

    public void close() {
        dbHelper.close();
        Log.i(TAG, "close()");
    }
    // Check if an alert already is added for the location:
    public boolean alertAlreadyExist(LocationDAO location){
        boolean exist = false;
        Cursor cursor = null;
        try{
            open();
            cursor = database.rawQuery("SELECT " + SQLiteDBHelper.C_ALERT_ID + " FROM " + table + " WHERE " + SQLiteDBHelper.C_ALERT_ID + " = '" + location.getId() + "'", null);
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                    exist = true;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        cursor.close();
        close();
        return exist;
    }
    // Deletes an alertsetting based on its id:
    public boolean deleteAlertSettings(int id){
        Log.i(TAG, "deleteAlertSettings: "+ id);
        boolean ok = false;
        try{
            open();
            ok = database.delete(table, SQLiteDBHelper.C_ALERT_ID + " = " + id, null) > 0;
            Log.i(TAG, "deleteAlertSettings: "+ ok);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return ok;
    }
    //Lagre varselinstillinger i database:
    public long insertAlertSettings(AlertSettingsDAO alert){
        Log.i(TAG, "insertAlertSettings()");
        long res = -1;
        try{
            open();
            ContentValues values = new ContentValues();
            values.put(SQLiteDBHelper.C_TEMPMIN, alert.getTempMin());
            values.put(SQLiteDBHelper.C_TEMPMAX, alert.getTempMax());
            values.put(SQLiteDBHelper.C_PRECIPITATIONMIN, alert.getPrecipitationMin());
            values.put(SQLiteDBHelper.C_PRECIPITATIONMAX, alert.getPrecipitationMax());
            values.put(SQLiteDBHelper.C_WINDSPEEDMIN, alert.getWindSpeedMin());
            values.put(SQLiteDBHelper.C_WINDSPEEDMAX, alert.getWindSpeedMax());
            values.put(SQLiteDBHelper.C_WINDIRECTION, alert.getWindDirection());
            values.put(SQLiteDBHelper.C_CHECKSUN, alert.isCheckSun());
            values.put(SQLiteDBHelper.C_CHECKINTERVAL, alert.getCheckInterval());
            values.put(SQLiteDBHelper.C_MON, alert.isMon());
            values.put(SQLiteDBHelper.C_TUE, alert.isTue());
            values.put(SQLiteDBHelper.C_WED, alert.isWed());
            values.put(SQLiteDBHelper.C_THU, alert.isThu());
            values.put(SQLiteDBHelper.C_FRI, alert.isFri());
            values.put(SQLiteDBHelper.C_SAT, alert.isSat());
            values.put(SQLiteDBHelper.C_SUN, alert.isSun());
            values.put(SQLiteDBHelper.C_LOC_ID, alert.getLocation().getId());
            res = database.insert(table, null, values);
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return res;
    }
    public long updateAlertSettings(AlertSettingsDAO alert){
        Log.i(TAG, "insertAlertSettings()");
        long res = -1;
        try{
            open();
            ContentValues values = new ContentValues();
            values.put(SQLiteDBHelper.C_TEMPMIN, alert.getTempMin());
            values.put(SQLiteDBHelper.C_TEMPMAX, alert.getTempMax());
            values.put(SQLiteDBHelper.C_PRECIPITATIONMIN, alert.getPrecipitationMin());
            values.put(SQLiteDBHelper.C_PRECIPITATIONMAX, alert.getPrecipitationMax());
            values.put(SQLiteDBHelper.C_WINDSPEEDMIN, alert.getWindSpeedMin());
            values.put(SQLiteDBHelper.C_WINDSPEEDMAX, alert.getWindSpeedMax());
            values.put(SQLiteDBHelper.C_WINDIRECTION, alert.getWindDirection());
            values.put(SQLiteDBHelper.C_CHECKSUN, alert.isCheckSun());
            values.put(SQLiteDBHelper.C_CHECKINTERVAL, alert.getCheckInterval());
            values.put(SQLiteDBHelper.C_MON, alert.isMon());
            values.put(SQLiteDBHelper.C_TUE, alert.isTue());
            values.put(SQLiteDBHelper.C_WED, alert.isWed());
            values.put(SQLiteDBHelper.C_THU, alert.isThu());
            values.put(SQLiteDBHelper.C_FRI, alert.isFri());
            values.put(SQLiteDBHelper.C_SAT, alert.isSat());
            values.put(SQLiteDBHelper.C_SUN, alert.isSun());
            res = database.update(table, values, "_id="+alert.getId(), null);
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return res;
    }
    public ArrayList<AlertSettingsDAO> getAllAlertSettings(){
        Log.i(TAG, "getAllAlertSettings()");
        ArrayList<AlertSettingsDAO> alertsettings = new ArrayList<>();
        Cursor res = null;
        try{
            open();
            res = database.rawQuery("select * from " + table + " limit 10", null);
            res.moveToFirst();
            while(!res.isAfterLast()) {
                AlertSettingsDAO alert = cursorToAlertSettings(res);
                // add to list
                alertsettings.add(alert);
                res.moveToNext();
            }
            res.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return alertsettings;
    }
    private AlertSettingsDAO cursorToAlertSettings(Cursor cursor) {
        AlertSettingsDAO alert = new AlertSettingsDAO(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6),
                cursor.getString(7),cursor.getInt(8),cursor.getDouble(9),cursor.getInt(10),cursor.getInt(11),cursor.getInt(12),cursor.getInt(13),cursor.getInt(14),cursor.getInt(15), cursor.getInt(16), null);
        LocationDAO location = new LocationDAO();
        location.setId(cursor.getInt(17));
        alert.setLocation(location);
        return alert;
    }
    public AlertSettingsDAO getAlertSettingById(int id){
        Log.i(TAG, "getAlertSettingById: "+ id);
        AlertSettingsDAO result = null;
        Cursor cursor;
        boolean ok = false;
        try{
            open();
            cursor = database.rawQuery("SELECT * FROM " + table + " WHERE _id = " + id,null);
            cursor.moveToFirst();
            result = cursorToAlertSettings(cursor);
            Log.i(TAG, "getAlertSettingById: "+ ok);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();


        return result;
    }
}
