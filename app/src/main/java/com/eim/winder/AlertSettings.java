package com.eim.winder;

/**
 * Created by Mari on 03.02.2016.
 */
public class AlertSettings {
    private Location location;
    private String temp;
    private double precipitationMin;
    private double precipitationMax;
    private double windSpeedMin;
    private double windSpeedMax;
    private String windDirection;
    private boolean Sun;
    private int checkInterval;

    public AlertSettings(){

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public boolean isSun() {
        return Sun;
    }

    public void setSun(boolean sun) {
        Sun = sun;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }
}
