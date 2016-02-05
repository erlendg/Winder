package com.eim.winder;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mari on 05.02.2016.
 */
public class SQLiteDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME ="winder.db";
    //Variables for table locations:
    public static final String TABLE_LOCATIONS = "locations";
    public static final String COLUMN_LOC_ID = "loc_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MUNICIPALITY = "municipality";
    public static final String COLUMN_COUNTY = "county";
    public static final String COLUMN_XMLURL = "xmlURL";
    //Variables for table alertsettings:
    public static final String TABLE_ALERTSETTINGS = "alertsettings";
    public static final String COLUMN_ALE_ID = "al_id";
    public static final String COLUMN_TEMP = "temp";
    public static final String COLUMN_PERCIPITATIONMIN = "percipitationmin";
    public static final String COLUMN_PERCIPITATIONMAX = "percipitationmax";
    public static final String COLUMN_WINDSPEEDMIN = "windSpeedmin";
    public static final String COLUMN_WINDSPEEDMAX = "windSpeedmax";
    public static final String COLUMN_WINDIRECTION = "winddirection";
    public static final String COLUMN_SUN ="sun";
    public static final String COLUM_CHECKINTERVAL = "checkinterval";

    private static final String locationQuery = "CREATE TABLE " + TABLE_LOCATIONS + "{" +
            COLUMN_LOC_ID + "INTEGER PRIMARY KEY AUTOINCREMENT" +
            COLUMN_NAME + "TEXT" +
            COLUMN_MUNICIPALITY + "TEXT" +
            COLUMN_COUNTY + "TEXT" +
            COLUMN_XMLURL + "TEXT" +
            "};";

    public SQLiteDatabase(Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    //Run when database gets created for the very first time:
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(locationQuery);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(db);
    }
    //Add new row to database
    public void addLocation(Location location){

    }
    // Delete a location from database:
    public void deleteLocation(String name){

    }
}
