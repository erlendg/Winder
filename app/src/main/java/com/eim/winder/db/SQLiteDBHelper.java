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
    public static final String L_LOCATION_ID = "_id";
    public static final String L_NAME = "name";
    public static final String L_TYPE = "type";
    public static final String L_MUNICIPALITY = "municipality";
    public static final String L_COUNTY = "county";
    public static final String L_XMLURL = "xmlurl";

    //Variables for table alertsettings:
    public static final String TABLE_ALERTSETTINGS = "alertsettings";
    public static final String A_ALERT_ID = "_id";
    public static final String A_TEMPMIN = "tempmin";
    public static final String A_TEMPMAX = "tempmax";
    public static final String A_PRECIPITATIONMIN = "precipitationmin";
    public static final String A_PRECIPITATIONMAX = "precipitationmax";
    public static final String A_WINDSPEEDMIN = "windspeedmin";
    public static final String A_WINDSPEEDMAX = "windspeedmax";
    public static final String A_WINDIRECTION = "winddirection";
    public static final String A_CHECKSUN ="checksun";
    public static final String A_CHECKINTERVAL = "checkinterval";
    public static final String A_MON = "mon";
    public static final String A_TUE = "tue";
    public static final String A_WED = "wed";
    public static final String A_THU = "thu";
    public static final String A_FRI = "fri";
    public static final String A_SAT = "sat";
    public static final String A_SUN = "sun";
    public static final String A_ICON_NAME ="iconname";
    public static final String A_HASEVENTS ="hasevents";
    public static final String A_LOC_ID = "loc_id";
    public static final String A_LASTUPDATE = "lastupdate";

    //Variables for table forecast:
    public static final String TABLE_FORECAST = "forecast";
    public static final String F_FORECAST_ID = "_id";
    public static final String F_FORMATEDDATE = "formatedDate";
    public static final String F_FORMATEDINFO = "formatedInfo";
    public static final String F_ICON = "icon";
    public static final String F_ALERT_ID = "alertsettings_id";
    

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
