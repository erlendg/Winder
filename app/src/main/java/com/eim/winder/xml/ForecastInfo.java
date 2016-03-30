package com.eim.winder.xml;

import java.util.ArrayList;

/**
 * Created by Erlend on 30.03.2016.
 */
public class ForecastInfo {
    private String locationName = "locationName";
    private String locationType = "type";
    private String locationCountry = "country";

    private int locationGeobaseID = 0;
    private double locationLatitude = 0.0;
    private double locationAltitude = 0.0;
    private double locationLongitude = 0.0;

    private String lastupdate  = "lastupdate";
    private String nextupdate = "nextupdate";

    private String sunrise = "sunrise";
    private String sunset = "sunset";
    private ArrayList<TabularInfo> tabularList = new ArrayList<>();

    public ForecastInfo(){

    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public int getLocationGeobaseID() {
        return locationGeobaseID;
    }

    public void setLocationGeobaseID(int locationGeobaseID) {
        this.locationGeobaseID = locationGeobaseID;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationAltitude() {
        return locationAltitude;
    }

    public void setLocationAltitude(double locationAltitude) {
        this.locationAltitude = locationAltitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getNextupdate() {
        return nextupdate;
    }

    public void setNextupdate(String nextupdate) {
        this.nextupdate = nextupdate;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public ArrayList<TabularInfo> getTabularList() {
        return tabularList;
    }

    public void setTabularList(ArrayList<TabularInfo> tabularList) {
        this.tabularList = tabularList;
    }

    public void addTabularInfoToList(TabularInfo info){
        tabularList.add(info);
    }
}
