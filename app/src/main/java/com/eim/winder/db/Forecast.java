package com.eim.winder.db;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Erlend on 08.04.2016.
 */
public class Forecast implements Parcelable {
    private int id;
    private String formatedDate;
    private String formatedInfo;
    private int icon;
    private int alertSettingId;

    public Forecast() {

    }
    public Forecast(String inDate){
        //"Onsdag, 11/05/16: 20.00 - 02.00"
        String formatedDate = "xxxxxx, " + inDate + ": 20.00 - 02.00";
        this.formatedDate = formatedDate;
    }

    public Forecast(int id, String formatedDate, String formatedInfo, int icon, int alertSettingId){
        this.id = id;
        this.formatedDate = formatedDate;
        this.formatedInfo = formatedInfo;
        this.icon = icon;
        this.alertSettingId = alertSettingId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormatedInfo() {
        return formatedInfo;
    }

    public void setFormatedInfo(String formatedInfo) {
        this.formatedInfo = formatedInfo;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getAlertSettingId() {
        return alertSettingId;
    }

    public void setAlertSettingId(int alertSettingId) {
        this.alertSettingId = alertSettingId;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(formatedDate);
        dest.writeString(formatedInfo);
        dest.writeInt(icon);
        dest.writeInt(alertSettingId);
    }

    Forecast(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        id = in.readInt();
        formatedDate = in.readString();
        formatedInfo = in.readString();
        icon = in.readInt();
        alertSettingId = in.readInt();
    }

    /**
    * This is needed for Android to be able to
    * create new objects, (or array of objects).
     * */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Forecast createFromParcel(Parcel in) {
                    return new Forecast(in);
                }

                public Forecast[] newArray(int size) {
                    return new Forecast[size];
                }
            };

    @Override
    public String toString() {
        return formatedDate+ " " +formatedInfo;
    }

    /**
     * takes a date on a certain format and turns it into an int
     * @param date
     * @return int on the yyyymmdd format
     */
    public int getStrippedDate(String date){
        //Log.i("FORECAST", "before getStrippedDate: " + date);
        String result = date.substring(date.length()-23,date.length()-15);

        String[] list = result.split("/");
        result = list[2] + list[1] + list[0];
        //Log.i("FORECAST", "getStrippedDate result: " + result);
        return Integer.parseInt(result);
    }

    /**
     * Standard compareTo-method for Forecast:
     * @param other other Forecast-object
     * @return -1 if this is smaller then other, 0 if equal, 1 if larger
     */
    public int compareTo(Forecast other){
        if (getStrippedDate(this.getFormatedDate())<getStrippedDate(other.getFormatedDate())){
            return -1;
        }
        else if (getStrippedDate(this.getFormatedDate())>getStrippedDate(other.getFormatedDate())){
            return 1;
        }
        else {
            return 0;
        }
    }
}
