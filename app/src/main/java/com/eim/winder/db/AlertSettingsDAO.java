package com.eim.winder.db;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.eim.winder.R;

/**
 * Created by Mari on 03.02.2016.
 */
public class AlertSettingsDAO implements Parcelable{
    public static int DEFAULT_TEMP = -274;
    public static int DEFAULT_WIND_AND_PRECIP = -1;
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
        this.tempMin = DEFAULT_TEMP;
        this.tempMax = DEFAULT_TEMP;
        this.precipitationMin = DEFAULT_WIND_AND_PRECIP;
        this.precipitationMax = DEFAULT_WIND_AND_PRECIP;
        this.windSpeedMin = DEFAULT_WIND_AND_PRECIP;
        this.windSpeedMax = DEFAULT_WIND_AND_PRECIP;
        this.windDirection = null;
        this.checkSun = false;
        this.mon = false;
        this.tue = false;
        this.wed = false;
        this.thu = false;
        this.fri = false;
        this.sat = false;
        this.sun = false;
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


    public String getWeekDaysSelected(Resources resources){
        String res = "";
        if(mon)res+= resources.getString(R.string.monday)+ " ";
        if(tue)res+= resources.getString(R.string.tuesday)+ " ";
        if(wed)res+= resources.getString(R.string.wednesday)+ " ";
        if(thu)res+= resources.getString(R.string.thursday)+ " ";
        if(fri)res+= resources.getString(R.string.friday)+ " ";
        if(sat)res+= resources.getString(R.string.saturday)+ " ";
        if(sun)res+= resources.getString(R.string.sunday);
        return res;
    }
    //Parcel-methodes and constructor:
    
    AlertSettingsDAO(Parcel in){
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        location = in.readParcelable(LocationDAO.class.getClassLoader());
        tempMin = in.readInt();
        tempMax = in.readInt();
        precipitationMin = in.readDouble();
        precipitationMax = in.readDouble();
        windSpeedMin = in.readDouble();
        windSpeedMax = in.readDouble();
        windDirection = in.readString();
        checkSun = in.readInt() > 0;
        checkInterval = in.readDouble();
        mon = in.readInt() > 0;
        tue = in.readInt() > 0;
        wed = in.readInt() > 0;
        thu = in.readInt() > 0;
        fri = in.readInt() > 0;
        sat = in.readInt() > 0;
        sun = in.readInt() > 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(location, flags);
        dest.writeInt(tempMin);
        dest.writeInt(tempMax);
        dest.writeDouble(precipitationMin);
        dest.writeDouble(precipitationMax);
        dest.writeDouble(windSpeedMin);
        dest.writeDouble(windSpeedMax);
        dest.writeString(windDirection);
        dest.writeInt(checkSun ? 1 : 0);
        dest.writeDouble(checkInterval);
        dest.writeInt(mon ? 1 : 0);
        dest.writeInt(tue ? 1 : 0);
        dest.writeInt(wed ? 1 : 0);
        dest.writeInt(thu ? 1 : 0);
        dest.writeInt(fri ? 1 : 0);
        dest.writeInt(sat ? 1 : 0);
        dest.writeInt(sun ? 1 : 0);
    }
    /*
    * This is needed for Android to be able to
    * create new objects, (or array of objects).
    * */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public AlertSettingsDAO createFromParcel(Parcel in) {
                    return new AlertSettingsDAO(in);
                }

                public AlertSettingsDAO[] newArray(int size) {
                    return new AlertSettingsDAO[size];
                }
            };
}
