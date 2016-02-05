package com.eim.winder;

/**
 * Created by Mari on 03.02.2016.
 */
public class Alert {
    private Location location;
    private String temp;
    private String windSpeed;
    private String windDirection;
    private boolean Sun;
    private int checkInterval;

    public Alert(){

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

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
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
