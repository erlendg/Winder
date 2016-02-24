package com.eim.winder.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mari on 08.02.2016.
 */
public class LocationDAO implements Parcelable{
    private int id;
    private String name;
    private String type;
    private String municipality;
    private String county;
    private String xmlURL;

    public LocationDAO(int id, String name, String type, String municipality, String county, String xmlURL) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.municipality = municipality;
        this.county = county;
        this.xmlURL = xmlURL;
    }
    public LocationDAO(){

    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getXmlURL() {
        return xmlURL;
    }

    public void setXmlURL(String xmlURL) {
        this.xmlURL = xmlURL;
    }

    LocationDAO(Parcel in){
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readString();
        municipality = in.readString();
        county = in.readString();
        xmlURL = in.readString();
    }

    @Override
    public String toString() {
        return getName() + ", " + getType() + ", (" + getMunicipality() + ", " + getCounty()+ ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // We just need to write each field into the
        // parcel. When we read from parcel, they
        // will come back in the same order
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(municipality);
        dest.writeString(county);
        dest.writeString(xmlURL);
    }
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public LocationDAO createFromParcel(Parcel in) {
                    return new LocationDAO(in);
                }

                public LocationDAO[] newArray(int size) {
                    return new LocationDAO[size];
                }
            };
}
