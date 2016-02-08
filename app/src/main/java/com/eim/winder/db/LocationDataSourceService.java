package com.eim.winder.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mari on 08.02.2016.
 */
public class LocationDataSourceService {
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
            database = dbHelper.getWritableDatabase();

    }

    public void close() {
        dbHelper.close();
    }
    public List<LocationDAO> getAllLocations() {
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
        Cursor cursor = getAllData();
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }

    public Cursor getAllData() {
        //SQLiteDatabase db = getReadableDatabase();
        Cursor res = database.rawQuery("select * from " + SQLiteDBHelper.TABLE_LOCATIONS + " limit 10", null);
        return res;
    }
    private LocationDAO cursorToLocation(Cursor cursor) {
        LocationDAO location = new LocationDAO(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return location;
    }

}
