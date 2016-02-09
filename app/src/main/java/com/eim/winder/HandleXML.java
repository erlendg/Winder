package com.eim.winder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by topkek on 09/02/16.
 */
public class HandleXML {

    private String name;
    private String type;
    private String country;
    private String timezoneId;
    private int utcoffsetMinutes;
    private int altitude;
    private double latitude;
    private double longitude;
    private String geobase;
    private int geobaseId;

    private String lastupdate;
    private String nextupdate;

    private String sunrise;
    private String sunset;

    private ArrayList<TabularInfo> forecastList;

    private String urlString;
    private XmlPullParserFactory xmlFactoryObj;
    private volatile boolean parsingComplete = true;

    public HandleXML(String url){
            this.urlString = url;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCountry() {
        return country;
    }

    public String getTimezoneId() {
        return timezoneId;
    }

    public int getUtcoffsetMinutes() {
        return utcoffsetMinutes;
    }

    public int getAltitude() {
        return altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getGeobase() {
        return geobase;
    }

    public int getGeobaseId() {
        return geobaseId;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public String getNextupdate() {
        return nextupdate;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public ArrayList<TabularInfo> getForecastList() {
        return forecastList;
    }

    public String getUrlString() {
        return urlString;
    }
    /*
    * Todo:
    *
    * implement parseAndStoreXML method adapted to the XML-structure provided by Yr.no
    *
    * */
    public void parseAndStoreXML(XmlPullParser myParser){
        int event;
        String text = null;

        try{
            event = myParser.getEventType();

            while(event != XmlPullParser.END_DOCUMENT){

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               try {
                   URL url = new URL(urlString);
                   HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                   connection.setReadTimeout(10000);
                   connection.setConnectTimeout(15000);
                   connection.setRequestMethod("GET");
                   connection.setDoInput(true);
                   connection.connect();

                   InputStream stream = connection.getInputStream();
                   xmlFactoryObj = XmlPullParserFactory.newInstance();
                   XmlPullParser myparser = xmlFactoryObj.newPullParser();

                   myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                   myparser.setInput(stream, null);

                   parseAndStoreXML(myparser);
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
