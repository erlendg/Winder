package com.eim.winder.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Erlend on 08.04.2016.
 */
public class ForecastDAO implements Parcelable {
    private int id;
    private String formatedInfo;
    private int icon;
    private int alertSettingId;

    public ForecastDAO() {

    }

    public ForecastDAO(int id, String formatedInfo, int icon, int alertSettingId) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(formatedInfo);
        dest.writeInt(icon);
        dest.writeInt(alertSettingId);
    }

    ForecastDAO(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        id = in.readInt();
        formatedInfo = in.readString();
        icon = in.readInt();
        alertSettingId = in.readInt();
    }

    /*
    * This is needed for Android to be able to
    * create new objects, (or array of objects).
    * */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public ForecastDAO createFromParcel(Parcel in) {
                    return new ForecastDAO(in);
                }

                public ForecastDAO[] newArray(int size) {
                    return new ForecastDAO[size];
                }
            };

    @Override
    public String toString() {
        return ""+formatedInfo;
    }
}
