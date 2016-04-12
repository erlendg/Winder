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
    public static final String C_LOCATION_ID = "_id";
    public static final String C_NAME = "name";
    public static final String C_TYPE = "type";
    public static final String C_MUNICIPALITY = "municipality";
    public static final String C_COUNTY = "county";
    public static final String C_XMLURL = "xmlURL";
    //Variables for table alertsettings:
    public static final String TABLE_ALERTSETTINGS = "alertsettings";
    public static final String C_ALERT_ID = "_id";
    public static final String C_TEMPMIN = "tempmin";
    public static final String C_TEMPMAX = "tempmax";
    public static final String C_PRECIPITATIONMIN = "precipitationmin";
    public static final String C_PRECIPITATIONMAX = "precipitationmax";
    public static final String C_WINDSPEEDMIN = "windspeedmin";
    public static final String C_WINDSPEEDMAX = "windspeedmax";
    public static final String C_WINDIRECTION = "winddirection";
    public static final String C_CHECKSUN ="checksun";
    public static final String C_CHECKINTERVAL = "checkinterval";
    public static final String C_MON = "mon";
    public static final String C_TUE = "tue";
    public static final String C_WED = "wed";
    public static final String C_THU = "thu";
    public static final String C_FRI = "fri";
    public static final String C_SAT = "sat";
    public static final String C_SUN = "sun";
    public static final String C_LOC_ID = "loc_id";

    //Variables for table forecast:
    public static final String TABLE_FORECAST = "forecast";
    public static final String F_FORECAST_ID = "_id";
    public static final String F_FORMATEDDATE = "formatedDate";
    public static final String F_FORMATEDINFO = "formatedInfo";
    public static final String F_ICON = "icon";
    public static final String F_ALERTSETTINGS_ID = "alertsettings_id";


    private static final String createLocationsQuery = "CREATE TABLE " + TABLE_LOCATIONS + "{" +
            C_LOCATION_ID + "INTEGER PRIMARY KEY AUTOINCREMENT" +
            C_NAME + "TEXT" +
            C_TYPE + "TEXT" +
            C_MUNICIPALITY + "TEXT" +
            C_COUNTY + "TEXT" +
            C_XMLURL + "TEXT" +
            "};";

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
