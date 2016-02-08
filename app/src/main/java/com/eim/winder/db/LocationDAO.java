package com.eim.winder.db;

/**
 * Created by Mari on 08.02.2016.
 */
public class LocationDAO {
    private long id;
    private String name;
    private String type;
    private String municipality;
    private String county;
    private String xmlURL;

    public LocationDAO(long id, String name, String type, String municipality, String county, String xmlURL) {
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

    public void setId(long id) {
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

    @Override
    public String toString() {
        return getName() + ", " + getType() + ", (" + getMunicipality() + ", " + getCounty()+ ")";
    }
}
