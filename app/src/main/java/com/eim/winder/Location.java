package com.eim.winder;

import android.support.design.widget.TabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Erlend on 01.02.2016.
 */
public class Location {
    private String name;
    private String municipality;
    private String county;
    private String xmlURL;

    public Location(String name, String municipality, String county, String xmlURL) {
        this.name = name;
        this.municipality = municipality;
        this.county = county;
        this.xmlURL = xmlURL;
    }

    public String getName() {
        return name;
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
}