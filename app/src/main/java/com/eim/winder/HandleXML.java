package com.eim.winder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by topkek on 09/02/16.
 */
public class HandleXML {
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
    private TabularInfo tabular;

    private String urlString = null;
    private int counter = 0;
    private boolean checkFlag = false;
    private boolean checkFlag2 = false;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public HandleXML(String url){
        this.urlString = url;
    }

    public String getLocationName() {
        return locationName;
    }

    public int getLocationGeobaseID() {
        return locationGeobaseID;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public ArrayList<TabularInfo> getTabularList() {
        return tabularList;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public double getLocationAltitude() {
        return locationAltitude;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public String getNextupdate() {
        return nextupdate;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        if (name.equalsIgnoreCase("location")) {

                            for (int i = 0; i<myParser.getAttributeCount(); i++) {
                                if (myParser.getAttributeName(i).equalsIgnoreCase("geobaseid")) {
                                    locationGeobaseID = Integer.parseInt(myParser.getAttributeValue(i));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("latitude")){
                                    locationLatitude = Double.parseDouble(myParser.getAttributeValue(i));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("altitude")){
                                    locationAltitude = Double.parseDouble(myParser.getAttributeValue(i));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("longitude")){
                                    locationLongitude = Double.parseDouble(myParser.getAttributeValue(i));
                                }
                            }
                        }
                        else if (name.equalsIgnoreCase("sun")){
                            for (int i = 0; i<myParser.getAttributeCount(); i++) {
                                if (myParser.getAttributeName(i).equalsIgnoreCase("rise")) {
                                    sunrise = myParser.getAttributeValue(i);
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("set")){
                                    sunset = myParser.getAttributeValue(i);
                                }
                            }
                        }
                        else if (name.equalsIgnoreCase("text")){
                            checkFlag=true;
                        }
                        else if (name.equalsIgnoreCase("time")){
                            if (!checkFlag) {
                                counter++;
                                tabular = new TabularInfo();
                                tabular.setCounter(counter);
                                for (int i = 0; i < myParser.getAttributeCount(); i++) {
                                    if (myParser.getAttributeName(i).equalsIgnoreCase("from")) {
                                        tabular.setFrom(myParser.getAttributeValue(i));
                                    } else if (myParser.getAttributeName(i).equalsIgnoreCase("to")) {
                                        tabular.setTo(myParser.getAttributeValue(i));
                                    } else if (myParser.getAttributeName(i).equalsIgnoreCase("period")) {
                                        tabular.setPeriod(Integer.parseInt(myParser.getAttributeValue(i)));
                                    }
                                }
                            }
                        }
                        else if (name.equalsIgnoreCase("symbol")){
                            for (int i = 0; i<myParser.getAttributeCount(); i++){
                                if (myParser.getAttributeName(i).equalsIgnoreCase("number")){
                                    tabular.setSymbolNumber(Integer.parseInt(myParser.getAttributeValue(i)));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("numberex")){
                                    tabular.setSymbolNumberEx(Integer.parseInt(myParser.getAttributeValue(i)));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("name")){
                                    tabular.setSymbolName(myParser.getAttributeValue(i));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("var")){
                                    tabular.setSymbolVar(myParser.getAttributeValue(i));
                                }

                            }
                        }
                        else if (name.equalsIgnoreCase("precipitation")){
                            for (int i = 0; i<myParser.getAttributeCount(); i++){
                                if(myParser.getAttributeName(i).equalsIgnoreCase("value")){
                                    tabular.setPrecipitationValue(Double.parseDouble(myParser.getAttributeValue(i)));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("minvalue")){
                                    tabular.setPrecipitationMin(Double.parseDouble(myParser.getAttributeValue(i)));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("maxvalue")){
                                    tabular.setPrecipitationMax(Double.parseDouble(myParser.getAttributeValue(i)));
                                }
                            }
                        }
                        else if (name.equalsIgnoreCase("windDirection")){
                            for (int i = 0; i<myParser.getAttributeCount(); i++){
                                if(myParser.getAttributeName(i).equalsIgnoreCase("deg")){
                                    tabular.setWindDirectionDeg(Double.parseDouble(myParser.getAttributeValue(i)));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("code")){
                                    tabular.setWindDirectionCode(myParser.getAttributeValue(i));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("name")){
                                    tabular.setWindDirectionName(myParser.getAttributeValue(i));
                                }
                            }
                        }
                        else if (name.equalsIgnoreCase("windspeed")){
                            for (int i = 0; i<myParser.getAttributeCount(); i++){
                                if(myParser.getAttributeName(i).equalsIgnoreCase("mps")){
                                    tabular.setWindSpeed(Double.parseDouble(myParser.getAttributeValue(i)));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("name")){
                                    tabular.setWindSpeedName(myParser.getAttributeValue(i));
                                }
                            }
                        }
                        else if (name.equalsIgnoreCase("temperature")){
                            if (!checkFlag2){
                                for (int i = 0; i<myParser.getAttributeCount(); i++){
                                    if(myParser.getAttributeName(i).equalsIgnoreCase("unit")){
                                        tabular.setTemperatureUnit(myParser.getAttributeValue(i));
                                    }
                                    else if (myParser.getAttributeName(i).equalsIgnoreCase("value")){
                                        tabular.setTemperatureValue(Double.parseDouble(myParser.getAttributeValue(i)));
                                    }
                                }
                            }
                        }
                        else if (name.equalsIgnoreCase("pressure")){

                            for (int i = 0; i<myParser.getAttributeCount(); i++){
                                if(myParser.getAttributeName(i).equalsIgnoreCase("unit")){
                                    tabular.setPressureUnit(myParser.getAttributeValue(i));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("value")){
                                    tabular.setPressureValue(Double.parseDouble(myParser.getAttributeValue(i)));
                                }
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if(name.equalsIgnoreCase("name")){
                            locationName = text;
                        }
                        else if (name.equalsIgnoreCase("text")){
                            checkFlag = false;
                        }
                        else if (name.equalsIgnoreCase("tabular")){
                            checkFlag2 = true;
                        }
                        else if (name.equalsIgnoreCase("type")){
                            locationType = text;
                        }
                        else if (name.equalsIgnoreCase("country")){
                            locationCountry = text;
                        }
                        else if(name.equalsIgnoreCase("time")){
                            if (!checkFlag) {
                                tabularList.add(tabular);
                            }
                        }
                        else if (name.equalsIgnoreCase("lastupdate")){
                            lastupdate = text;
                        }
                        else if (name.equalsIgnoreCase("nextupdate")){
                            nextupdate = text;
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}