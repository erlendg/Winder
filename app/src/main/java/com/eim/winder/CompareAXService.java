package com.eim.winder;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.ArrayAdapter;

import com.eim.winder.db.AlertSettingsDAO;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Erlend on 19.02.2016.
 */
public class CompareAXService {
    private HandleXML xmlHandlerObj;
    private AlertSettingsDAO alertSettingsObj;
    private boolean onCreateSuccess = false;
    private boolean[] occurenceListIntervals;
    private boolean sendNotification;

    public CompareAXService(AlertSettingsDAO alertSettingsObj){
        this.alertSettingsObj = alertSettingsObj;

        try {
            System.err.println("url: " + alertSettingsObj.getLocation().getXmlURL());
            xmlHandlerObj = new HandleXML(alertSettingsObj.getLocation().getXmlURL());
            onCreateSuccess =true;
        }
        catch (Exception e){
            System.out.println("Error most likely due to empty LocationDAO for AlertSettingsDAO");
            onCreateSuccess = false;
        }
    }


    public boolean getOnCreateSuccess(){
        return onCreateSuccess;
    }

    public boolean runHandleXML(){
        xmlHandlerObj.fetchXML();
        while(xmlHandlerObj.parsingComplete);
        return true;

    }
    public boolean findAllOccurences(){
        //todo: implement a loop to compare all TabularInfo objects in HandleXML with the settings defined within AlertSettingsDAO, and populate the bool array.
        occurenceListIntervals = new boolean[xmlHandlerObj.getTabularList().size()];
        Arrays.fill(occurenceListIntervals, false);
        sendNotification = false;
        for (int i = 0; i<xmlHandlerObj.getTabularList().size(); i++){

            occurenceListIntervals[i] = findOccurence(xmlHandlerObj.getTabularList().get(i));
            if(occurenceListIntervals[i]){
                sendNotification = true;
            }
        }

        return sendNotification;
    }

    private boolean findOccurence(TabularInfo div){
        //todo: implement all comparison method calls and return either true if there is an occurence, or false if not.

        boolean checkFlag = false;
        /*

        if(checkPrecipitation(div.getPrecipitationValue())) checkFlag = true;
        if(checkSymbolSun()) checkFlag = true;
        if (checkWindDirection(div.getWindDirectionName())) checkFlag = true;
        if (checkWindSpeed(div.getWindSpeed())) checkFlag = true;
        */
        if(checkTemp(div.getTemperatureValue())) checkFlag = true;

        return checkFlag;
    }

    //private int tempMin;
    //private int tempMax;
    public boolean checkTemp(double value){
        if ((double)(alertSettingsObj.getTempMin())<(value)&&(value<(double)(alertSettingsObj.getTempMax()))) return true;
        else return false;
    }
    //private double precipitationMin;
    //private double precipitationMax;
    public boolean checkPrecipitation(double value){
        if ((alertSettingsObj.getPrecipitationMin())<(value)&&(value<(alertSettingsObj.getPrecipitationMax()))) return true;
        else return false;
    }
    //private double windSpeedMin;
    //private double windSpeedMax;
    public boolean checkWindSpeed(double value){
        if ((alertSettingsObj.getWindSpeedMin())<(value)&&(value<(alertSettingsObj.getWindSpeedMax()))) return true;
        else return false;
    }

    //private String windDirection;
    public boolean checkWindDirection(String a){
        if (alertSettingsObj.getWindDirection().equalsIgnoreCase(a)) return true;
        else return false;
    }
    //private boolean checkSun;
    public boolean checkSymbolSun(){
        if (alertSettingsObj.isCheckSun()) return true;
        else return false;
    }
    //private double checkInterval;

    //private boolean mon, tue, wed, thu, fri, sat, sun;




}
