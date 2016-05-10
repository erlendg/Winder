package com.eim.winder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Erlend on 08.04.2016.
 * Repository-class for Forecast. Handles database-transactions needed for Forecast
 */
public class ForecastRepo {
    private final String TAG = "ForecastRepo";
    private SQLiteDatabase database;
    private SQLiteDBHelper dbHelper;
    private Context context;
    private String table = SQLiteDBHelper.TABLE_FORECAST;
    private String[] allColumns = {SQLiteDBHelper.F_FORECAST_ID, SQLiteDBHelper.F_FORMATEDDATE, SQLiteDBHelper.F_FORMATEDINFO, SQLiteDBHelper.F_ICON, SQLiteDBHelper.F_ALERT_ID};

    public ForecastRepo(Context context){
        this.context = context;
        dbHelper = new SQLiteDBHelper(context);
    }

    /**
     * For testing purposes only.
     * @param context to initialize the database
     * @param sqLiteDBHelper database created
     */
    public ForecastRepo(Context context, SQLiteDBHelper sqLiteDBHelper){
        this.context = context;
        dbHelper = sqLiteDBHelper;
    }

    /**
     * get Readable instance of database:
     * @return database-object
     */
    public SQLiteDatabase getReadDB(){
        Log.i(TAG, "getReadDB()");
        return dbHelper.getReadableDatabase();
    }

    /**
     * get Writable instance of database:
     * @return database-object
     */
    private SQLiteDatabase getWriteDB(){
        Log.i(TAG, "getWriteDB()");
        return dbHelper.getWritableDatabase();
    }

    /**
     * close database-connection
     */
    public void close(){
        dbHelper.close();
        Log.i(TAG, "close()");
    }

    /**
     * cursorToForecast takes a Cursor containing a row from the database, and transfers the data to a Forecast-object
     * @param c Cursor-object
     * @return populated Forecast-object
     */
    private Forecast cursorToForecast(Cursor c){
        return new Forecast(c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.F_FORECAST_ID)),
                c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.F_FORMATEDDATE)),
                c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.F_FORMATEDINFO)),
                c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.F_ICON)),
                c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.F_ALERT_ID)));
    }

    /**
     * Get first Forecast for an AlertSetting by its ID
     * @param id id of AlertSettings-object
     * @return instance of Forecast found in the database
     */
    public Forecast getForecastByAlertSettingsID(int id){
        SQLiteDatabase db = getReadDB();
        Cursor c = db.rawQuery("SELECT * FROM " + table + " WHERE alertsettings_id = " + id, null);
        c.moveToFirst();
        Forecast result = cursorToForecast(c);
        c.close();
        close();
        return result;
    }

    /**
     * returns an ArrayList of Forecast-objects from the database, based on AlertSettingsID
     * @param id id of AlertSettings-object
     * @return ArrayList of Forecast-objects
     */
    public ArrayList<Forecast> getAllForecastsByAlertSettingsID(int id){
        ArrayList<Forecast> list = new ArrayList<>();
        Forecast temp;
        SQLiteDatabase db = getReadDB();
        Cursor c = db.rawQuery("SELECT * FROM " + table + " WHERE alertsettings_id =" + id, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            temp = cursorToForecast(c);
            list.add(temp);
            c.moveToNext();
        }
        c.close();
        close();
        return list;
    }

    /**
     * checks if there are any rows in the database with the specified AlertSettingsID
     * @param id id of AlertSettingsObject
     * @return boolean, true if exists, false if not
     */
    public boolean findIfForecastsExistsForAlertSettingsID(int id){
        boolean result = true;
        SQLiteDatabase db = getReadDB();
        Cursor c = db.rawQuery("SELECT * FROM " + table + " WHERE alertsettings_id =" + id, null);
        if (c.getCount() == 0) result = false;
        Log.i(TAG,"findIfForecastsExistsForAlertSettingsID("+id+")");
        c.close();
        close();
        return result;
    }

    /**
     * deletes all Forecast-rows with the specified id
     * @param id id of AlertSettings-Object
     * @return true if successfull, false if not
     */
    public boolean deleteForecastByAlertSettingsID(int id){
        SQLiteDatabase db = getWriteDB();
        boolean ok = db.delete(table, SQLiteDBHelper.F_ALERT_ID + " = " + id, null) > 0;
        Log.i(TAG, "deleteForecastByAlertSettingsID("+id+"): " + ok);
        close();
        return ok;
    }

    /**
     * inserts list of Forecast-objects into the database, after deleting all the old forecast-objects for this id.
     * @param forecastList list of Forecastobjects
     * @param id id of the related AlertSettings-object
     * @return
     */
    public boolean insertForecastList(ArrayList<Forecast> forecastList, int id){
        deleteForecastByAlertSettingsID(id);
        boolean ok = true;
        SQLiteDatabase db = getWriteDB();
        for (Forecast temp:forecastList) {
            ContentValues values = new ContentValues();
            values.put(SQLiteDBHelper.F_FORMATEDDATE, temp.getFormatedDate());
            values.put(SQLiteDBHelper.F_FORMATEDINFO, temp.getFormatedInfo());
            values.put(SQLiteDBHelper.F_ICON, temp.getIcon());
            values.put(SQLiteDBHelper.F_ALERT_ID, temp.getAlertSettingId());
            long res = db.insert(table, null, values);
            if (res == -1) {
                ok = false;
                break;
            }
        }
        close();
        Log.i(TAG, "insertforecastList: " + ok);
        return ok;
    }

}

