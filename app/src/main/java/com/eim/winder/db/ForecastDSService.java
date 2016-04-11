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
public class ForecastDSService {
    private final String TAG = "ForecastDSService";
    private SQLiteDatabase database;
    private SQLiteDBHelper dbHelper;
    private String table = SQLiteDBHelper.TABLE_FORECAST;
    private String[] allColumns = {SQLiteDBHelper.F_FORECAST_ID, SQLiteDBHelper.F_FORMATEDINFO, SQLiteDBHelper.F_ICON, SQLiteDBHelper.F_ALERTSETTINGS_ID};

    public ForecastDSService(Context context){
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
    private ForecastDAO cursorToForecast(Cursor cursor){
        ForecastDAO forecast = new ForecastDAO(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
        return forecast;
    }
    public ForecastDAO getForecastByAlertSettingsID(int id){
        ForecastDAO result = null;
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
        return result;
    }

    public ArrayList<ForecastDAO> getAllForecastsByAlertSettingsID(int id){
        ArrayList<ForecastDAO> list = new ArrayList<>();
        Cursor cursor = null;
        try{
            open();
            cursor = database.rawQuery("SELECT * FROM " + table + " WHERE alertsettings_id =" + id, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                ForecastDAO temp = cursorToForecast(cursor);
                list.add(temp);
                cursor.moveToNext();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteAllForecastsByAlertSettingsID(int id){
        boolean ok = false;

        return ok;
    }

    public boolean deleteForecastByAlertSettingsID(int id){
        boolean ok = false;
        try{
            open();
            ok = database.delete(table, SQLiteDBHelper.F_ALERTSETTINGS_ID + " = " + id, null) > 0;
            Log.i(TAG, "deleteForecastByAlertSettingsID " + ok);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return ok;
    }

    public long insertForecast(ForecastDAO forecast){

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
    }

    public boolean insertForecastList(ArrayList<ForecastDAO> forecastList, int id){
        deleteForecastByAlertSettingsID(id);
        long res = -1;
        boolean ok = true;
        for (ForecastDAO temp:forecastList){
            res = insertForecast(temp);
            if (res==-1){
                ok=false;
                break;
            }
        }
        Log.i(TAG, "insertforecastList: " + ok);
        return ok;
    }

}

