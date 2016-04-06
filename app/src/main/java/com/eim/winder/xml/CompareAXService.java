package com.eim.winder.xml;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettingsDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Erlend on 19.02.2016.
 */
public class CompareAXService {
    private HandleXML xmlHandlerObj;
    private AlertSettingsDAO alertSettingsObj;
    private ForecastInfo forecast;
    private boolean onCreateSuccess = false;
    private boolean sendNotification;
    private Calendar c;
    private Date d;
    private final static String tag = "CompareAXService";
    private boolean tempCheck, precipitationCheck, sunCheck, windDirectionCheck, windSpeedCheck;
    String url;
    private NotificationCompat.Builder notification;

    public CompareAXService(AlertSettingsDAO alertSettingsObj){
        this.alertSettingsObj = alertSettingsObj;
        this.forecast = new ForecastInfo();
        this.url = alertSettingsObj.getLocation().getXmlURL();

        try {
            System.err.println("url: " +  url);
            xmlHandlerObj = new HandleXML(url, forecast);
            onCreateSuccess =true;
        }
        catch (Exception e){
            System.out.println("Error most likely due to empty LocationDAO for AlertSettingsDAO");
            onCreateSuccess = false;
        }
    }
    public CompareAXService(AlertSettingsDAO alertSettingsObj, String url){
        this.alertSettingsObj = alertSettingsObj;
        this.forecast = new ForecastInfo();
        this.url = url;

        try {
            System.err.println("url: " +  url);
            xmlHandlerObj = new HandleXML(url, forecast);
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

    /**
     *Generates and displays one notification if there has been an occurence for the specific alertsetting.
     *
     * @param a list of values
     * @param i alertsettingID
     * @param cont context of activity that called the method.
     * @param cl class of activity
     * @param nm notificationmanager injected from previously mentioned activity
     */
    public void generateNotification(ArrayList<String> a, int i, Context cont, Class cl, NotificationManager nm){
        notification = new NotificationCompat.Builder(cont);
        notification.setSmallIcon(R.drawable.testicon);
        if(!a.isEmpty()) {
            notification.setContentTitle("Vi har en match.");
            notification.setContentText("for område " + i + "!");
        }
        else{
            notification.setContentTitle("Ingen hendelser");
            notification.setContentText("for område " +i+ "!");
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(cont, cl);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(cont);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(cl);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setContentIntent(resultPendingIntent);

        /* MÅ OPPRETTES I KONTEKSTEN DER MAN BENYTTER KODEN:
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(cont.NOTIFICATION_SERVICE);
        */
        // mId allows you to update the notification later on.
        nm.notify(i, notification.build());

    }

    /**
     *
     * @param info
     * @return
     */
    private String generateInfo(TabularInfo info){
        String returnString = "";
        returnString += "Fra: " + info.getFrom() + ", til: " + info.getTo() + "\n";
                        // grader celcius utf8-kode: "\u2103";
        if (tempCheck){
            returnString += "Temperatur: " + info.getTemperatureValue() + "\u2103\n";
        }
        if(sunCheck){
            returnString += "Det er meldt klarvær!\n";
        }
        if(precipitationCheck){
            returnString += "Nedbør: " + info.getPrecipitationValue() + "mm" + "\n";
        }
        if(windDirectionCheck){
            returnString += "Vindretning: " + info.getWindDirectionName() + "\n";
        }
        if (windSpeedCheck){
            returnString += "Vindstyrke: " + info.getWindSpeed() + "m/s \n";
        }
        Log.i(tag, returnString);
        return returnString;
    }
    public boolean runHandleXML(){
        xmlHandlerObj.fetchXML();
        while(xmlHandlerObj.parsingComplete);
        return true;

    }
    public ArrayList<String> findAllOccurences(){
        //todo: implement a loop to compare all TabularInfo objects in HandleXML with the settings defined within AlertSettingsDAO, and populate the bool array.

        //siden ukedag ikke er implementert, sett sjekk for alle dager:
        alertSettingsObj.setMon(true);
        alertSettingsObj.setTue(true);
        alertSettingsObj.setWed(true);
        alertSettingsObj.setThu(true);
        alertSettingsObj.setFri(true);
        alertSettingsObj.setSat(true);
        alertSettingsObj.setSun(true);



        ArrayList<TabularInfo> list = forecast.getTabularList();
        ArrayList<String> returnList = new ArrayList<>();
        for (int i = 0; i<forecast.getTabularList().size(); i++){
            sendNotification = findOccurence(list.get(i));

            if (sendNotification){
                returnList.add(generateInfo(list.get(i)));
            }
            /*
            else if(!sendNotification){

            }
            */
        }

        return returnList;
    }

    private boolean findOccurence(TabularInfo div){
        //todo: implement all comparison method calls and return either true if there is an occurence, or false if not.

        if(checkPrecipitation(div.getPrecipitationValue())==2) return false;

        if(checkSymbolSun(div.getSymbolName())== 2) return false;

        //if (checkWindDirection(div.getWindDirectionName())==2) return false;

        if (checkWindSpeed(div.getWindSpeed())==2) return false;

        if(checkTemp(div.getTemperatureValue())== 2) return false;

        /*
        switch (checkWeekday(div.getFrom())){
            case 1:
                if (!alertSettingsObj.isMon()) return false;
                break;
            case 2:
                if (!alertSettingsObj.isTue()) return false;
                break;
            case 3:
                if (!alertSettingsObj.isWed()) return false;
                break;
            case 4:
                if (!alertSettingsObj.isThu()) return false;
                break;
            case 5:
                if (!alertSettingsObj.isFri()) return false;
                break;
            case 6:
                if (!alertSettingsObj.isSat()) return false;
                break;
            case 7:
                if (!alertSettingsObj.isSun()) return false;
                break;
            default:
                break;
        }
        */


        return true;
    }
    public int checkWeekday(String date){
        c = Calendar.getInstance();

        try {
            d = new SimpleDateFormat("yyyy-MM-ddThh:mm:ss").parse(date);
        }
        catch (Exception e){
            System.out.println("div datofeil");
        }

        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }
    //private int tempMin;
    //private int tempMax;
    public int checkTemp(double value){

        if ((double)(alertSettingsObj.getTempMin())<(value)&&(value<(double)(alertSettingsObj.getTempMax()))) {
            //Log.d(tag, "Temp returverdi = 0, innverdi = " + value);
            tempCheck = true;
            return 0;
        }
        if(alertSettingsObj.getTempMin() == -274){
            //Log.d(tag, "Temp returverdi = 1, innverdi = " + value);
            return 1;
        }
        //Log.d(tag, "Temp returverdi = 2, innverdi = " + value);
        return 2;
    }
    //private double precipitationMin;
    //private double precipitationMax;
    public int checkPrecipitation(double value) {
        if ((alertSettingsObj.getPrecipitationMin()) < (value) && (value < (alertSettingsObj.getPrecipitationMax()))){
            //Log.d(tag, "Nedbør returverdi = 0, innverdi = " + value);
            precipitationCheck = true;
            return 0;
        }
        if(alertSettingsObj.getPrecipitationMax() == -1){
            //Log.d(tag, "Nedbør returverdi = 1, innverdi = " + value);
            return 1;
        }
        //Log.d(tag, "Nedbør returverdi = 2, innverdi = " + value);
        return 2;
    }
    //private double windSpeedMin;
    //private double windSpeedMax;
    public int checkWindSpeed(double value){
        if ((alertSettingsObj.getWindSpeedMin())<(value)&&(value<(alertSettingsObj.getWindSpeedMax()))) {
            //Log.d(tag, "Vindstyrke returverdi = 0, innverdi = " + value);
            windSpeedCheck = true;
            return 0;
        }
        if(alertSettingsObj.getWindSpeedMin() == -1) {
            //Log.d(tag, "Vindstyrke returverdi = 1, innverdi = " + value);
            return 1;
        }
        //Log.d(tag, "Vindstyrke returverdi = 2, innverdi = " + value);
        return 2;
    }

    //private String windDirection;
    public int checkWindDirection(String a) {
        if (alertSettingsObj.getWindDirection() == null){
            //Log.d(tag, "Vindretning returverdi = 1, innverdi = " + a);
            windDirectionCheck = true;
            return 1;
        }
        if (alertSettingsObj.getWindDirection().equalsIgnoreCase(a)){
           // Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
            return 0;
        }

        //Log.d(tag, "Vindretning returverdi = 2, innverdi = " + a);
        return 2;
    }
    //private boolean checkSun;
    public int checkSymbolSun(String a) {
        //Log.d(tag, "innhold i SunString: " + a);

        if (alertSettingsObj.isCheckSun() && a.equalsIgnoreCase("clear sky")){
            //Log.d(tag, "Sol returverdi = 0, innverdi = " + a);
            sunCheck = true;
            return 0;
        }
        if (!alertSettingsObj.isCheckSun()){
            //Log.d(tag, "sol returverdi 1, innverdi = " +a);
            return 1;
        }
            //Log.d(tag, "Sol returverdi = 2, innverdi = " + a);
            return 2;

        //return 0;
    }
    //private double checkInterval;

    //private boolean mon, tue, wed, thu, fri, sat, sun;




}
