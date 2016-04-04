package com.eim.winder.xml;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by topkek on 09/02/16.
 */
public class HandleXML {

    private ForecastInfo forecast;
    private TabularInfo tabular;

    private String urlString = null;
    private int counter = 0;
    private boolean checkFlag = false;
    private boolean checkFlag2 = false;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public HandleXML(String url, ForecastInfo a){
        this.urlString = url;
        this.forecast = a;
    }

    /**
     *
     * @param myParser
     */
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
                                    forecast.setLocationGeobaseID(Integer.parseInt(myParser.getAttributeValue(i)));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("latitude")){
                                    forecast.setLocationLatitude(Double.parseDouble(myParser.getAttributeValue(i)));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("altitude")){
                                    forecast.setLocationAltitude(Double.parseDouble(myParser.getAttributeValue(i)));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("longitude")){
                                    forecast.setLocationLongitude(Double.parseDouble(myParser.getAttributeValue(i)));
                                }
                            }
                        }
                        else if (name.equalsIgnoreCase("sun")){
                            for (int i = 0; i<myParser.getAttributeCount(); i++) {
                                if (myParser.getAttributeName(i).equalsIgnoreCase("rise")) {
                                    forecast.setSunrise(myParser.getAttributeValue(i));
                                }
                                else if (myParser.getAttributeName(i).equalsIgnoreCase("set")){
                                    forecast.setSunset(myParser.getAttributeValue(i));
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
                            forecast.setLocationName(text);
                        }
                        else if (name.equalsIgnoreCase("text")){
                            checkFlag = false;
                        }
                        else if (name.equalsIgnoreCase("tabular")){
                            checkFlag2 = true;
                        }
                        else if (name.equalsIgnoreCase("type")){
                            forecast.setLocationType(text);
                        }
                        else if (name.equalsIgnoreCase("country")){
                            forecast.setLocationCountry(text);
                        }
                        else if(name.equalsIgnoreCase("time")){
                            if (!checkFlag) {
                                forecast.addTabularInfoToList(tabular);
                            }
                        }
                        else if (name.equalsIgnoreCase("lastupdate")){
                            forecast.setLastupdate(text);
                        }
                        else if (name.equalsIgnoreCase("nextupdate")){
                            forecast.setNextupdate(text);
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

    public ForecastInfo getForecastInfo(){
        return forecast;
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