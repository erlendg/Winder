package com.eim.winder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Erlend on 08.04.2016.
 */
public class ForecastRepo {
    private final String TAG = "ForecastRepo";
    private SQLiteDatabase database;
    private SQLiteDBHelper dbHelper;
    private String table = SQLiteDBHelper.TABLE_FORECAST;
    private String[] allColumns = {SQLiteDBHelper.F_FORECAST_ID, SQLiteDBHelper.F_FORMATEDDATE, SQLiteDBHelper.F_FORMATEDINFO, SQLiteDBHelper.F_ICON, SQLiteDBHelper.F_ALERT_ID};

    public ForecastRepo(Context context){
        this.dbHelper = new SQLiteDBHelper(context);
    }

    public void open() throws SQLException{
        Log.i(TAG, "open()");
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
        Log.i(TAG, "close()");
    }
    private Forecast cursorToForecast(Cursor cursor){
        Forecast forecast = new Forecast(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4));
        return forecast;
    }
    public Forecast getForecastByAlertSettingsID(int id){
        Forecast result = null;
        Cursor cursor;
        boolean ok = false;
        try{
            open();
            cursor = database.rawQuery("SELECT * FROM " + table + " WHERE alertsettings_id = " + id, null);
            cursor.moveToFirst();
            result = cursorToForecast(cursor);
        } catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return result;
    }

    public ArrayList<Forecast> getAllForecastsByAlertSettingsID(int id){
        ArrayList<Forecast> list = new ArrayList<>();
        Cursor cursor = null;
        Forecast temp;
        try{
            open();
            cursor = database.rawQuery("SELECT * FROM " + table + " WHERE alertsettings_id =" + id, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                temp = cursorToForecast(cursor);
                list.add(temp);
                cursor.moveToNext();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return list;
    }

    public boolean findIfForecastsExistsForAlertSettingsID(int id){
        boolean result = true;
        Cursor cursor = null;
        try{
            open();
            cursor = database.rawQuery("SELECT * FROM " + table + " WHERE alertsettings_id =" + id, null);
            if (cursor.getCount() == 0) result = false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean deleteAllForecastsByAlertSettingsID(int id){
        boolean ok = false;

        return ok;
    }

    public boolean deleteForecastByAlertSettingsID(int id){
        boolean ok = false;
        try{
            open();
            ok = database.delete(table, SQLiteDBHelper.F_ALERT_ID + " = " + id, null) > 0;
            Log.i(TAG, "deleteForecastByAlertSettingsID " + ok);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return ok;
    }

    /*public long insertForecast(Forecast forecast){

        long res = -1;
        try{
            open();
            ContentValues values = new ContentValues();
            //values.put(SQLiteDBHelper.F_FORECAST_ID, forecast.getId());
            values.put(SQLiteDBHelper.F_FORMATEDINFO, forecast.getFormatedInfo());
            values.put(SQLiteDBHelper.F_ICON, forecast.getIcon());
            values.put(SQLiteDBHelper.F_ALERTSETTINGS_ID, forecast.getAlertSettingId());
            res = database.insert(table, null, values);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return res;
    }*/

    public boolean insertForecastList(ArrayList<Forecast> forecastList, int id){
        deleteForecastByAlertSettingsID(id);
        long res = -1;
        boolean ok = true;
        try{

            open();
            for (Forecast temp:forecastList) {
                ContentValues values = new ContentValues();
                values.put(SQLiteDBHelper.F_FORMATEDDATE, temp.getFormatedDate());
                values.put(SQLiteDBHelper.F_FORMATEDINFO, temp.getFormatedInfo());
                values.put(SQLiteDBHelper.F_ICON, temp.getIcon());
                values.put(SQLiteDBHelper.F_ALERT_ID, temp.getAlertSettingId());
                res = database.insert(table, null, values);
                if (res == -1) {
                    ok = false;
                    break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
        Log.i(TAG, "insertforecastList: " + ok);
        return ok;
    }

}

