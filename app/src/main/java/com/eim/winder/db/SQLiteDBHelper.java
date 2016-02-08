package com.eim.winder.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Mari on 05.02.2016.
 */
public class SQLiteDBHelper extends SQLiteAssetHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME ="winder.db";

    //Variables for table locations:
    public static final String TABLE_LOCATIONS = "locations";
    public static final String COLUMN_LOCATION_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_MUNICIPALITY = "municipality";
    public static final String COLUMN_COUNTY = "county";
    public static final String COLUMN_XMLURL = "xmlURL";
    //Variables for table alertsettings:
    public static final String TABLE_ALERTSETTINGS = "alertsettings";
    public static final String COLUMN_ALERT_ID = "_id";
    public static final String COLUMN_TEMP = "temp";
    public static final String COLUMN_PERCIPITATIONMIN = "percipitationmin";
    public static final String COLUMN_PERCIPITATIONMAX = "percipitationmax";
    public static final String COLUMN_WINDSPEEDMIN = "windspeedmin";
    public static final String COLUMN_WINDSPEEDMAX = "windspeedmax";
    public static final String COLUMN_WINDIRECTION = "winddirection";
    public static final String COLUMN_SUN ="sun";
    public static final String COLUM_CHECKINTERVAL = "checkinterval";

    private static final String createLocationsQuery = "CREATE TABLE " + TABLE_LOCATIONS + "{" +
            COLUMN_LOCATION_ID + "INTEGER PRIMARY KEY AUTOINCREMENT" +
            COLUMN_NAME + "TEXT" +
            COLUMN_TYPE + "TEXT" +
            COLUMN_MUNICIPALITY + "TEXT" +
            COLUMN_COUNTY + "TEXT" +
            COLUMN_XMLURL + "TEXT" +
            "};";

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
