package com.eim.winder.db;

/**
 * Created by Mari on 03.02.2016.
 */
public class AlertSettingsDAO {
    private LocationDAO location;
    private int id;
    private int tempMin;
    private int tempMax;
    private double precipitationMin;
    private double precipitationMax;
    private double windSpeedMin;
    private double windSpeedMax;
    private String windDirection;
    private boolean checkSun;
    private double checkInterval;
    private boolean mon, tue, wed, thu, fri, sat, sun;

    public AlertSettingsDAO(int id, int tempMin, int tempMax, double precipitationMin, double precipitationMax, double windSpeedMin, double windSpeedMax, String windDirection, int checkSun, double checkInterval, int mon, int tue, int wed, int thu, int fri, int sat, int sun, LocationDAO location) {
        this.id = id;
        this.location = location;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.precipitationMin = precipitationMin;
        this.precipitationMax = precipitationMax;
        this.windSpeedMin = windSpeedMin;
        this.windSpeedMax = windSpeedMax;
        this.windDirection = windDirection;
        this.checkSun = checkSun > 0;
        this.checkInterval = checkInterval;
        this.mon = mon > 0 ;
        this.tue = tue > 0;
        this.wed = wed > 0;
        this.thu = thu > 0;
        this.fri = fri > 0;
        this.sat = sat > 0;
        this.sun = sun > 0;
    }

    public AlertSettingsDAO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWindSpeedMin() {
        return windSpeedMin;
    }

    public void setWindSpeedMin(double windSpeedMin) {
        this.windSpeedMin = windSpeedMin;
    }

    public double getWindSpeedMax() {
        return windSpeedMax;
    }

    public void setWindSpeedMax(double windSpeedMax) {
        this.windSpeedMax = windSpeedMax;
    }

    public double getPrecipitationMin() {
        return precipitationMin;
    }

    public void setPrecipitationMin(double precipitationMin) {
        this.precipitationMin = precipitationMin;
    }

    public double getPrecipitationMax() {
        return precipitationMax;
    }

    public void setPrecipitationMax(double precipitationMax) {
        this.precipitationMax = precipitationMax;
    }

    public LocationDAO getLocation() {
        return location;
    }

    public void setLocation(LocationDAO location) {
        this.location = location;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public boolean isCheckSun() {
        return checkSun;
    }

    public void setCheckSun(boolean checkSun) {
        this.checkSun = checkSun;
    }

    public double getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(double checkInterval) {
        this.checkInterval = checkInterval;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }
}
