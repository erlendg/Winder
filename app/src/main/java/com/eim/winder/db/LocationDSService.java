package com.eim.winder.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mari on 08.02.2016.
 */
public class LocationDSService {
    public final static String TAG = "LocationDSService";
    private SQLiteDatabase database;
    private SQLiteDBHelper dbHelper;
    private String table = SQLiteDBHelper.TABLE_LOCATIONS;
    private String[] allColumns = { SQLiteDBHelper.C_LOCATION_ID,SQLiteDBHelper.C_LOCATION_ID, SQLiteDBHelper.C_NAME,
            SQLiteDBHelper.C_TYPE, SQLiteDBHelper.C_MUNICIPALITY, SQLiteDBHelper.C_COUNTY, SQLiteDBHelper.C_XMLURL};

    public LocationDSService(Context context) { //Dependency injection.
        dbHelper = new SQLiteDBHelper(context);
    }

    public void open()throws SQLException{
        Log.i(TAG, "open()");
            database = dbHelper.getWritableDatabase();

    }

    public void close() {
        dbHelper.close();
        Log.i(TAG, "close()");
    }

    public ArrayList<LocationDAO> getAllLocations() {
        Log.i(TAG, "getAllLocations");
        ArrayList<LocationDAO> locations = new ArrayList<LocationDAO>();
        Cursor res = null;
        try{
            open();
            res = database.rawQuery("select * from " + SQLiteDBHelper.TABLE_LOCATIONS + " ORDER BY " + SQLiteDBHelper.C_NAME + " ASC", null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                LocationDAO location = cursorToLocation(res);
                locations.add(location);
                res.moveToNext();
            }
            res.close();
            // make sure to close the cursor
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return locations;
    }
    public String[] getArray(){
        Log.i(TAG, "getArray()");
        Cursor cursor = getFirstTenData();
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }
    public LocationDAO getLocationFromID(int id){
        Cursor cursor;
        LocationDAO location = null;
        try{
            open();
            cursor = database.query(table,null,SQLiteDBHelper.C_LOCATION_ID + " = ?", new String[]{""+id}, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                location = cursorToLocation(cursor);
                // add to list
                cursor.moveToNext();
            }
            cursor.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return location;
    }

    private LocationDAO cursorToLocation(Cursor cursor) {
        LocationDAO location = new LocationDAO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return location;
    }
    //Read records related to the search term:
    public ArrayList<LocationDAO> readSearch(String searchTerm) {
        Log.i(TAG, "readSearch()");
        ArrayList<LocationDAO> recordsList = new ArrayList<LocationDAO>();
        // select query
        String sql = "";
        sql += "SELECT * FROM " + SQLiteDBHelper.TABLE_LOCATIONS;
        sql += " WHERE " + SQLiteDBHelper.C_NAME + " LIKE '" + searchTerm + "%'";
        //sql += " OR " + SQLiteDBHelper.COLUMN_TYPE + " LIKE '" + searchTerm + "%'";
        sql += " ORDER BY " + SQLiteDBHelper.C_NAME + " ASC";
       //sql += " LIMIT 0,20";
        // execute the query
        try{
            open();
            Cursor cursor = database.rawQuery(sql, null);
            // looping through all rows and adding to list
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                LocationDAO location = cursorToLocation(cursor);
                // add to list
                recordsList.add(location);
                cursor.moveToNext();
            }
            cursor.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        // return the list of records
        return recordsList;
    }

    public Cursor getFirstTenData() {
        Log.i(TAG, "getFirstTenData()");
        Cursor res = null;
        try{
            open();
            res = database.rawQuery("select * from " + SQLiteDBHelper.TABLE_LOCATIONS + " limit 10", null);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }
    // Check if a location exists
    public boolean checkIfExists(String objectName){
        boolean recordExists = false;
        Cursor cursor = database.rawQuery("SELECT " + SQLiteDBHelper.C_NAME + " FROM " + SQLiteDBHelper.TABLE_LOCATIONS + " WHERE " + SQLiteDBHelper.C_NAME + " = '" + objectName + "'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                recordExists = true;
            }
        }
        cursor.close();
        return recordExists;
    }

}
