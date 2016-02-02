package com.eim.winder;

import java.util.ArrayList;

/**
 * Created by Erlend on 01.02.2016.
 */
public class Location {
    private String name;
    private String type;
    private String country;
    private String timeZoneID;
    private int utcOffsetMinutes;
    private int altitude;
    private double latitude;
    private double longitude;
    private String geoBase;
    private int geoBaseID;
    private ArrayList<TextInfo> textList;
    private ArrayList<TabularInfo> tabularList;

    public Location(String name, String type, String country, String timeZoneID, int utcOffsetMinutes, int altitude, double latitude, double longitude, String geoBase, int geoBaseID) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.timeZoneID = timeZoneID;
        this.utcOffsetMinutes = utcOffsetMinutes;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoBase = geoBase;
        this.geoBaseID = geoBaseID;
    }

    public Location(){

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCountry() {
        return country;
    }

    public String getTimeZoneID() {
        return timeZoneID;
    }

    public int getUtcOffsetMinutes() {
        return utcOffsetMinutes;
    }

    public int getAltitude() {
        return altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getGeoBase() {
        return geoBase;
    }

    public int getGeoBaseID() {
        return geoBaseID;
    }

    public ArrayList<TextInfo> getTextList() {
        return textList;
    }

    public ArrayList<TabularInfo> getTabularList() {
        return tabularList;
    }
}
