package com.eim.winder.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mari on 08.02.2016.
 * Repo class for Location. Handel the connection to the Location table in database
 */
public class LocationRepo {
    public final static String TAG = "LocationRepo";
    private SQLiteDBHelper dbHelper;
    private Context context;
    private String table = SQLiteDBHelper.TABLE_LOCATIONS;
    private String[] allColumns = { SQLiteDBHelper.L_LOCATION_ID, SQLiteDBHelper.L_NAME,
            SQLiteDBHelper.L_TYPE, SQLiteDBHelper.L_MUNICIPALITY, SQLiteDBHelper.L_COUNTY, SQLiteDBHelper.L_XMLURL};

    public LocationRepo(Context context) {
        this.context = context;
        dbHelper = new SQLiteDBHelper(context);
    }
    /**
     * For testing purposes only
     * @param context to initialize the database
     * @param sqLiteDBHelper database created
     */
    public LocationRepo(Context context, SQLiteDBHelper sqLiteDBHelper){
        this.context = context;
        dbHelper = sqLiteDBHelper;
    }

    /**
     * @return writable database
     */
    private SQLiteDatabase getReadDB(){
        //Log.i(TAG, "getReadDB()");
        return dbHelper.getReadableDatabase();
    }

    /**
     * Close database connection
     */
    public void close() {
        dbHelper.close();
        Log.i(TAG, "close()");
    }

    /**
     * @return ArrayList of all locations in the pre-populated database
     */
    public ArrayList<Location> getAllLocations() {
        SQLiteDatabase db = getReadDB();
        //Log.i(TAG, "getAllLocations");
        ArrayList<Location> locations = new ArrayList();
        Cursor c = db.query(table, null, null, null, null, null, SQLiteDBHelper.L_NAME + " ASC");
        //res = database.rawQuery("select * from " + SQLiteDBHelper.TABLE_LOCATIONS + " ORDER BY " + SQLiteDBHelper.L_NAME + " ASC", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Location location = cursorToLocation(c);
            locations.add(location);
            c.moveToNext();
        }
        // make sure to close the cursor
        c.close();
        close();
        return locations;
    }

    /**
     * @param id of location
     * @return A Location object based on id
     */
    public Location getLocationFromID(int id){
        Location location = null;
        SQLiteDatabase db = getReadDB();
        Cursor c = db.query(table, null, SQLiteDBHelper.L_LOCATION_ID + " = ?", new String[]{"" + id}, null, null, null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            location = cursorToLocation(c);
            // add to list
            c.moveToNext();
        }
        c.close();
        close();
        return location;
    }

    /**
     * Finds a Location object based on id and an open database connection
     * @param id of Location in database
     * @param db database connection
     * @return A Location object based on id
     */
    public Location getLocationFromID(int id, SQLiteDatabase db){
        Cursor c;
        Location location = null;
        c = db.query(table,null,SQLiteDBHelper.L_LOCATION_ID + " = ?", new String[]{""+id}, null, null, null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            location = cursorToLocation(c);
            c.moveToNext();
        }
        c.close();
        return location;
    }
    /**
     * Helping method to parse cursor values and store them inside an Location object
     * @param c cursor
     * @return Location object
     */
    private Location cursorToLocation(Cursor c) {
        return  new Location(c.getInt(c.getColumnIndexOrThrow(SQLiteDBHelper.L_LOCATION_ID)),
                c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.L_NAME)),
                c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.L_TYPE)),
                c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.L_MUNICIPALITY)),
                c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.L_COUNTY)),
                c.getString(c.getColumnIndexOrThrow(SQLiteDBHelper.L_XMLURL)));
    }

    /**
     * Read records related to the search term:
     * For testing only. the table contains about 11 000 pre populated location objects.
     * this query returns a stripped ArrayList of objects based on the search term.
     * Not used, at the moment the system handles a list of 11 000 objects, but when the list
     * grows this method wil be essential for the system response.
     * @param searchTerm name of the location in the database
     * @return List of Location objects
     */
    public ArrayList<Location> readSearch(String searchTerm) {
        Log.i(TAG, "readSearch()");
        ArrayList<Location> recordsList = new ArrayList();
        // select query
        String sql = "";
        sql += "SELECT * FROM " + SQLiteDBHelper.TABLE_LOCATIONS;
        sql += " WHERE " + SQLiteDBHelper.L_NAME + " LIKE '" + searchTerm + "%'";
        //sql += " OR " + SQLiteDBHelper.COLUMN_TYPE + " LIKE '" + searchTerm + "%'";
        sql += " ORDER BY " + SQLiteDBHelper.L_NAME + " ASC";
       //sql += " LIMIT 0,20";
        // execute the query
        SQLiteDatabase db = getReadDB();
        Cursor c = db.rawQuery(sql, null);
        // looping through all rows and adding to list
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Location location = cursorToLocation(c);
            // add to list
            recordsList.add(location);
            c.moveToNext();
        }
        c.close();
        close();
        // return the list of records
        return recordsList;
    }

}
