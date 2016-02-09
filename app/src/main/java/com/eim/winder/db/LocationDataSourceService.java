package com.eim.winder.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mari on 08.02.2016.
 */
public class LocationDataSourceService {
    public final static String TAG = "LocationDSService";
    private SQLiteDatabase database;
    private SQLiteDBHelper dbHelper;
    private String[] allColumns = { SQLiteDBHelper.COLUMN_LOCATION_ID,SQLiteDBHelper.COLUMN_LOCATION_ID, SQLiteDBHelper.COLUMN_NAME,
            SQLiteDBHelper.COLUMN_TYPE, SQLiteDBHelper.COLUMN_MUNICIPALITY, SQLiteDBHelper.COLUMN_COUNTY, SQLiteDBHelper.COLUMN_XMLURL};

    public LocationDataSourceService(Context context) { //Dependency injection.
        dbHelper = new SQLiteDBHelper(context);
        try{
            open();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void open()throws SQLException{
        Log.i(TAG, "open()");
            database = dbHelper.getWritableDatabase();

    }

    public void close() {
        dbHelper.close();
        Log.i(TAG, "close()");
    }

    public List<LocationDAO> getAllLocations() {

        Log.i(TAG, "getAllLocations");
        List<LocationDAO> locations = new ArrayList<LocationDAO>();

        Cursor cursor = database.query(SQLiteDBHelper.TABLE_LOCATIONS,
                allColumns, null, null, null, null, "10");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocationDAO location = cursorToLocation(cursor);
            locations.add(location);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
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

    private LocationDAO cursorToLocation(Cursor cursor) {
        LocationDAO location = new LocationDAO(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return location;
    }
    //Read records related to the search term:
    public List<LocationDAO> readSearch(String searchTerm) {
        Log.i(TAG, "readSearch()");
        List<LocationDAO> recordsList = new ArrayList<LocationDAO>();
        // select query
        String sql = "";
        sql += "SELECT * FROM " + SQLiteDBHelper.TABLE_LOCATIONS;
        sql += " WHERE " + SQLiteDBHelper.COLUMN_NAME + " LIKE '" + searchTerm + "%'";
        //sql += " OR " + SQLiteDBHelper.COLUMN_TYPE + " LIKE '" + searchTerm + "%'";
        sql += " ORDER BY " + SQLiteDBHelper.COLUMN_NAME + " ASC";
        sql += " LIMIT 0,20";
        // execute the query
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
        // return the list of records
        return recordsList;
    }

    public Cursor getFirstTenData() {
        Log.i(TAG, "getFirstTenData()");
        Cursor res = database.rawQuery("select * from " + SQLiteDBHelper.TABLE_LOCATIONS + " limit 10", null);
        return res;
    }
    // Check if a location exists
    public boolean checkIfExists(String objectName){
        boolean recordExists = false;
        Cursor cursor = database.rawQuery("SELECT " + SQLiteDBHelper.COLUMN_NAME + " FROM " + SQLiteDBHelper.TABLE_LOCATIONS + " WHERE " + SQLiteDBHelper.COLUMN_NAME + " = '" + objectName + "'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                recordExists = true;
            }
        }
        cursor.close();
        return recordExists;
    }

}
