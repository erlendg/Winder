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
public class AlertSettingsRepo {
    public final static String TAG = "AlertSettingsRepo";
    private SQLiteDatabase database;
    private SQLiteDBHelper dbHelper;
    private String table = SQLiteDBHelper.TABLE_ALERTSETTINGS;
    private String[] allColumns = { SQLiteDBHelper.A_ALERT_ID, SQLiteDBHelper.A_TEMPMIN, SQLiteDBHelper.A_TEMPMAX,
            SQLiteDBHelper.A_PRECIPITATIONMIN, SQLiteDBHelper.A_PRECIPITATIONMAX, SQLiteDBHelper.A_WINDSPEEDMIN,
            SQLiteDBHelper.A_WINDSPEEDMAX, SQLiteDBHelper.A_WINDIRECTION, SQLiteDBHelper.A_CHECKSUN, SQLiteDBHelper.A_CHECKINTERVAL,
            SQLiteDBHelper.A_MON, SQLiteDBHelper.A_TUE, SQLiteDBHelper.A_WED, SQLiteDBHelper.A_THU,
            SQLiteDBHelper.A_FRI, SQLiteDBHelper.A_SAT, SQLiteDBHelper.A_SUN, SQLiteDBHelper.A_ICON_NAME};

    public AlertSettingsRepo(Context context) {
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
            cursor = database.rawQuery("SELECT " + SQLiteDBHelper.A_ALERT_ID + " FROM " + table + " WHERE " + SQLiteDBHelper.A_ALERT_ID + " = '" + location.getId() + "'", null);
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
            ok = database.delete(table, SQLiteDBHelper.A_ALERT_ID + " = " + id, null) > 0;
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
            values.put(SQLiteDBHelper.A_LOC_ID, alert.getLocation().getId());
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
                cursor.getString(7),cursor.getInt(8),cursor.getDouble(9),cursor.getInt(10),cursor.getInt(11),cursor.getInt(12),cursor.getInt(13),cursor.getInt(14),cursor.getInt(15), cursor.getInt(16), cursor.getString(17), null);
        LocationDAO location = new LocationDAO();
        location.setId(cursor.getInt(18));
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
