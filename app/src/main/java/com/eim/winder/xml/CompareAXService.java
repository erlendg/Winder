package com.eim.winder.xml;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.AlertSettingsRepo;
import com.eim.winder.db.DBService;
import com.eim.winder.db.Forecast;
import com.eim.winder.db.ForecastRepo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Erlend on 19.02.2016.
 */
public class CompareAXService {
    private final String TAG = "CompareAXService";

    private HandleXML xmlHandlerObj;
    private AlertSettings alertSettingsObj;
    private ForecastInfo forecast;
    private boolean onCreateSuccess = false;
    private boolean sendNotification;
    private Calendar c;
    private Date d,d2;
    private boolean tempCheck, precipitationCheck, sunCheck, windDirectionCheck, windSpeedCheck;
    String url;
    private NotificationCompat.Builder notification;
    private ForecastRepo forecastRepo;
    private AlertSettingsRepo alertSettingsRepo;
    private DBService dbService;
    Context context;

    public CompareAXService(){

    }

    public CompareAXService(Context context, AlertSettings alertSettingsObj){
        this.context = context;
        this.alertSettingsObj = alertSettingsObj;
        this.forecast = new ForecastInfo();
        this.url = alertSettingsObj.getLocation().getXmlURL();
        this.forecastRepo = new ForecastRepo(context);
        this.alertSettingsRepo = new AlertSettingsRepo(context);
        DBService dbService = new DBService(alertSettingsRepo, forecastRepo);


        try {
            System.err.println("url: " +  url);
            xmlHandlerObj = new HandleXML(url, forecast);
            onCreateSuccess =true;
        }
        catch (Exception e){
            System.out.println("Error most likely due to empty Location for AlertSettings");
            onCreateSuccess = false;
        }
    }

    public CompareAXService(Context context, AlertSettings alertSettingsObj, String url){
        this.context = context;
        this.alertSettingsObj = alertSettingsObj;
        this.forecast = new ForecastInfo();
        this.url = url;
        this.forecastRepo = new ForecastRepo(context);
        this.alertSettingsRepo = new AlertSettingsRepo(context);
        DBService dbService = new DBService(alertSettingsRepo, forecastRepo);

        try {
            System.err.println("url: " +  url);
            xmlHandlerObj = new HandleXML(url, forecast);
            onCreateSuccess =true;
        }
        catch (Exception e){
            System.out.println("Error most likely due to empty Location for AlertSettings");
            onCreateSuccess = false;
        }
    }


    public boolean getOnCreateSuccess(){
        return onCreateSuccess;
    }

    /**
     *Generates and displays one notification if there has been an occurence for the specific alertsetting.
     *
     * @param i alertsettingID
     * @param locName location name for the current alertSetting
     * @param context context of activity that called the method.
     * @param cl class of activity
     * @param nm notificationmanager injected from previously mentioned activity
     * @param type indicates which kind of notification should be issued
     */
    public void generateNotification(int i, String locName, Context context, Class cl, NotificationManager nm, int type){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notification = new NotificationCompat.Builder(context);
                notification.setSmallIcon(R.drawable.ic_stat_name);
                notification.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
                notification.setSound(alarmSound);
                //notification.setVibrate(new long[]{1000, 1000, 1000, 1000});
                notification.setLights(Color.BLUE, 3000, 3000);

                if (type == 1){
                    notification.setContentTitle(context.getResources().getString(R.string.notification_message_title_success));
                    notification.setContentText(context.getResources().getString(R.string.notification_message_text) + " " + locName);
                }else if(type == 2){
                    notification.setContentTitle(context.getResources().getString(R.string.notification_message_title_no_success));
                    notification.setContentText(context.getResources().getString(R.string.notification_message_text)+ " " + locName);
                }else if (type == 3){
                    notification.setContentTitle(context.getResources().getString(R.string.notification_message_title_additional_events));
                    notification.setContentText(context.getResources().getString(R.string.notification_message_text)+ " " + locName);
                }

                // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(context, cl);

                // The stack builder object will contain an artificial back stack for the
                // started Activity.ivity leads out of
                // your application to the Home screen.
                // This ensures that navigating backward from the Act
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(cl);

                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                notification.setContentIntent(resultPendingIntent);
                notification.setAutoCancel(true);

                /* MÅ OPPRETTES I KONTEKSTEN DER MAN BENYTTER KODEN:
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(cont.NOTIFICATION_SERVICE);
                */
                // mId allows you to update the notification later on.
                nm.notify(i, notification.build());


    }

    /**generateInfo prepares the date- and info-parameter to be stored in Forecast.
     *
     * @param info
     * @return String[2] where index[1] is the date, and index[2] is the info
     */
    private String[] generateInfo(TabularInfo info){
        String returnString = "";
        String[] returnTable = new String[2];
        returnTable[0] = fixDate(info.getFrom(), info.getTo());
        if(tempCheck){
            returnString += context.getResources().getString(R.string.generate_temperature)+ " " + info.getTemperatureValue() + "\u2103\n"; // grader celcius utf8-kode: "\u2103";

        }
        if(sunCheck){
            returnString += context.getResources().getString(R.string.generate_clear)+"\n";
        }
        if(precipitationCheck){
            returnString += context.getResources().getString(R.string.generate_precipitatiion)+ " " + info.getPrecipitationValue() + "mm" + "\n";
        }
        if(windDirectionCheck || windSpeedCheck){

            String input = "";
            switch (info.getWindDirectionCode()){
                //<string name="N">North</string>
                case "N":
                    input = context.getResources().getString(R.string.N);
                    break;
                //<string name="NNW">North-Northwest</string>
                case "NNW":
                    input = context.getResources().getString(R.string.NNW);
                    break;
                //<string name="NW">Northwest</string>
                case "NW":
                    input = context.getResources().getString(R.string.NW);
                    break;
                //<string name="WNW">West-Northwest</string>
                case "WNW":
                    input = context.getResources().getString(R.string.WNW);
                    break;
                //<string name="W">West</string>
                case "W":
                    input = context.getResources().getString(R.string.W);
                    break;
                //<string name="WSW">West-Southwest</string>
                case "WSW":
                    input = context.getResources().getString(R.string.WSW);
                    break;
                //<string name="SW">Southwest</string>
                case "SW":
                    input = context.getResources().getString(R.string.SW);
                    break;
                //<string name="SSW">South-Southwest</string>
                case "SSW":
                    input = context.getResources().getString(R.string.SSW);
                    break;
                //<string name="S">South</string>
                case "S":
                    input = context.getResources().getString(R.string.S);
                    break;
                //<string name="SSE">South-Southeast</string>
                case "SSE":
                    input = context.getResources().getString(R.string.SSE);
                    break;
                //<string name="SE">Southeast</string>
                case "SE":
                    input = context.getResources().getString(R.string.SE);
                    break;
                //<string name="ESE">East-Southeast</string>
                case "ESE":
                    input = context.getResources().getString(R.string.ESE);
                    break;
                //<string name="E">East</string>
                case "E":
                    input = context.getResources().getString(R.string.E);
                    break;
                //<string name="ENE">East-Northeast</string>
                case "ENE":
                    input = context.getResources().getString(R.string.ENE);
                    break;
                //<string name="NE">Northeast</string>
                case "NE":
                    input = context.getResources().getString(R.string.NE);
                    break;
                //<string name="NNE">North-Northeast</string>
                case "NNE":
                    input = context.getResources().getString(R.string.NNE);
                    break;
                default:

                    break;
            }
            returnString += context.getResources().getString(R.string.generate_winddirection) + " " + input + "\n";
        }
        if (windSpeedCheck){
            returnString += context.getResources().getString(R.string.generate_windspeed)+ " " + info.getWindSpeed() + "m/s \n";
        }
        Log.i(TAG, returnString);
        returnTable[1] = returnString.trim();
        return  returnTable;
    }

    /**fixDate takes two date-strings and returns a time-period.
     *
     * @param fromDate
     * @param toDate
     * @return String containing the formated date
     */
    private String fixDate(String fromDate, String toDate){
        String result = "";
        switch (checkWeekday(fromDate)){
            case 1:
                result += context.getResources().getString(R.string.weekdays_sunday);
                break;
            case 2:
                result += context.getResources().getString(R.string.weekdays_monday);
                break;
            case 3:
                result += context.getResources().getString(R.string.weekdays_tuesday);
                break;
            case 4:
                result += context.getResources().getString(R.string.weekdays_wednesday);
                break;
            case 5:
                result += context.getResources().getString(R.string.weekdays_thursday);
                break;
            case 6:
                result += context.getResources().getString(R.string.weekdays_friday);
                break;
            case 7:
                result += context.getResources().getString(R.string.weekdays_saturday);
                break;
            default:
                break;
        }       //"/" + fromDate.substring(5,7)
        result += ", " + fromDate.substring(8,10) + "/" + fromDate.substring(5,7) + "/" + fromDate.substring(2,4) + ": " + fromDate.substring(11,13) + "." + fromDate.substring(14,16) + " - " + toDate.substring(11,13) + "." + toDate.substring(14,16) ;

        return result;
    }

    /**
     * fixDate(String date)  converts the date to our preferred format for display
     * @param date
     * @return String containing the formated date
     */
    private String fixDate(Date date, Locale current){
        SimpleDateFormat formatter;
        if (current.getCountry().equalsIgnoreCase("no")){
            formatter = new SimpleDateFormat("EEEE dd/MM HH:mm", current);

        }else{
            formatter = new SimpleDateFormat("EEEE MMM dd hh:mm aa", current);
        }

        String result = formatter.format(date);



        return result;
    }

    /**checkWeekday determines weekday of a specific date.
     *
     * @param date date to be checked for weekday, yr.no specified format
     * @return integer 1-7, where 1 = sunday
     */
    private int checkWeekday(String date){
        c = Calendar.getInstance();

        try {
            d = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(date);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("div datofeil");
        }

        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }
    /**isNetworkOnline checks currently available network-options.
     *
     * @return integer where 0 = no connection available, 1= wifi connection available, 2=mobiledata connection available
     */
    public int isNetworkOnline() {
        int status=0;
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //NetworkInfo netInfo = cm.getNetworkInfo(0);
            NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= 1;
            }else {
                //netInfo = cm.getNetworkInfo(1);
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= 2;
            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
        Log.i(TAG, "Result from isNetworkOnline : " + status);
        return status;

    }

    /**
     * runHandleXML checks for internet-connections, and determines if a new xml-file should be fetched.
     *
     * @return true or false depending on successfully parsing the latest xml-file.
     */
    public boolean runHandleXML(){

        int currentConnection = isNetworkOnline();
        //if no connection is present, return false:
        if (currentConnection==0){
            return false;
        }

        //find settings for mobiledata as a connection:
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean useMobileData = sp.getBoolean("prefUseMobileData", true);
        Log.i(TAG, "Currently prefUseMobileData is : " + useMobileData);

        //if mobileconnection available, and useMobileData is false, return false
        if (currentConnection==2 && !useMobileData){
            return false;
        }
        //then just run stuff
        xmlHandlerObj.fetchXML();
        while(xmlHandlerObj.parsingComplete);
        return true;

    }

    /** updateAlertSettingsEvent updates this AlertSettings' hasEvents parameter
     *
     * @param id of the AlertSetting
     * @param hasEvents value of the current hasEvents-state
     */
    private void updateAlertSettingsEvent(int id, int hasEvents){
            alertSettingsRepo.updateAlertsettingsHasEvents(id, hasEvents);
    }

    /** addNewForecastsToDB stores a complete list of Forecast-objects to the database
     *
     * @param list of Forecast-objects
     */
    private void addNewForecastsToDB(final ArrayList<Forecast> list){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                DBService dbService = new DBService(forecastRepo);
                dbService.addForecastList(list, alertSettingsObj.getId());

            }
        });
        thread.start();

        boolean ok = false;
    }

    /** findAllOccurences uses the data collected from the xml-source and
     *
     * @param id id of alertsettings-object
     * @param locName name of location
     * @param context context of the activity
     * @param cl class of the activity
     * @param nm NotificationManager for issuing notifications to the user
     * @return integer based on the outcome of the method, 1-4 currently.
     */
    public int findAllOccurences(int id, String locName, Context context, Class cl, NotificationManager nm){

        ArrayList<TabularInfo> list = forecast.getTabularList();
        ArrayList<Forecast> returnList = new ArrayList<>();
        Forecast temp;
        String[] dateandinfo;
        int result = -1;

        //goes through the list of new forecast-data to look for matches based on the AlertSettingsobjects parameters
        for (int i = 0; i<list.size(); i++){
            sendNotification = findOccurence(list.get(i));

            if (sendNotification){
                //gather the required information and adds it to the Forecast-list
                dateandinfo = generateInfo(list.get(i));
                temp = new Forecast();
                temp.setAlertSettingId(alertSettingsObj.getId());
                temp.setFormatedDate(dateandinfo[0]);
                temp.setFormatedInfo(dateandinfo[1]);
                temp.setIcon(list.get(i).getSymbolNumber());
                returnList.add(temp);
            }

        }
        //for the dynamic event-display, update hasEvents for AlertSettings
        if(returnList.isEmpty()){
            updateAlertSettingsEvent(id, 0);
        } else {
            updateAlertSettingsEvent(id, 1);
        }

        //this is where we determine which notification we want to issue to the user, based on the int return value:
        if(!forecastRepo.findIfForecastsExistsForAlertSettingsID(id)){
            //Case 1: New Forecast-entries found from new XML, but no previous Forecast-entries are found in the database(DB)
            if(!returnList.isEmpty()){
                Log.e(TAG, "CASE1");

                addNewForecastsToDB(returnList);
                generateNotification(id, locName, context, cl, nm, 1);

                return 1;
            } else{
                //Case 2: No new Forecast-entries found from new XML, and no previous Forecast-entries are found in the DB
              // do nothing atm
                Log.e(TAG, "CASE2");
                return 2;
            }
        } else{
            if (!returnList.isEmpty()) {
                //Case 3: New Forecast-entries found from new XML, and previous Forecast-entries are found in the DB
                //Fetch old database-entries:
                ArrayList<Forecast> oldList = forecastRepo.getAllForecastsByAlertSettingsID(id);
                //here we need to figure out if the new set of entries are significantly different from the old set to determine if issuing a new notification is warranted:
                int stuff = findIfNewEvents(oldList, returnList);
                if (stuff == 1){
                        //this means the entire new list occurs after the old list
                        generateNotification(id, locName, context, cl, nm, 3);
                        addNewForecastsToDB(returnList);
                        return 3;
                }else if (stuff== 2){
                        //this means earlier events have been discovered
                        generateNotification(id, locName, context, cl, nm, 3);
                        addNewForecastsToDB(returnList);
                        return 3;
                }else if (stuff == 3){
                        //this means later events have been discovered
                        generateNotification(id, locName, context, cl, nm, 3);
                        addNewForecastsToDB(returnList);
                        return 3;
                }else if (stuff == 4){
                    //this means new events have been found inbetween
                    generateNotification(id, locName, context, cl, nm, 3);
                    addNewForecastsToDB(returnList);
                    return 3;
                }

                //no new events discovered, no new notification:
                addNewForecastsToDB(returnList);
                Log.e(TAG, "CASE3");
                return 3;
            }else{
                //Case 4: No new Forecast-entries found from new XML, and previous Forecast-entries are found in the DB
                forecastRepo.deleteForecastByAlertSettingsID(id);
                generateNotification(id, locName, context, cl, nm, 2);
                Log.e(TAG, "CASE4");
                return 4;
            }
        }
    }

    /** findOccurence takes a TabularInfo object and determines if it matches the AlertSettings parameters
     *
     * @param div TabularInfo object containing fresh weather-data
     * @return boolean where true = match, false = no match
     */
    private boolean findOccurence(TabularInfo div){

        if(checkPrecipitation(div.getPrecipitationValue())==2) return false;

        if(checkSymbolSun(div.getSymbolName())== 2) return false;

        if (checkWindDirection(div.getWindDirectionCode())==2) return false;

        if (checkWindSpeed(div.getWindSpeed())==2) return false;

        if(checkTemp(div.getTemperatureValue())== 2) return false;


        //weekday 1 is sunday:
        switch (checkWeekday(div.getFrom())){
            case 1:
                if (!alertSettingsObj.isSun()) return false;
                break;
            case 2:
                if (!alertSettingsObj.isMon()) return false;
                break;
            case 3:
                if (!alertSettingsObj.isTue()) return false;
                break;
            case 4:
                if (!alertSettingsObj.isWed()) return false;
                break;
            case 5:
                if (!alertSettingsObj.isThu()) return false;
                break;
            case 6:
                if (!alertSettingsObj.isFri()) return false;
                break;
            case 7:
                if (!alertSettingsObj.isSat()) return false;
                break;
            default:
                break;
        }

        Log.e(TAG, "resultat fra checkWeekday: " + checkWeekday(div.getFrom()));

        return true;
    }

    /**findIfNewEvents compares two lists of Forecast-objects to determine if a new notification to the user is needed
     *
     * @param oldList list of old Forecast-objects fetched from the database
     * @param newList list of new Forecast-objects generated from XML
     * @return int based on the outcome of the control-structure, 1 - 4, -1 if no new events
     */
    public int findIfNewEvents(ArrayList<Forecast> oldList, ArrayList<Forecast> newList){
        int result = -1;
        if (newList.get(0).compareTo(oldList.get(oldList.size()-1))== 1){
            result = 1;
        }else if (newList.get(0).compareTo(oldList.get(0)) == -1){
            result = 2;
        }else if (newList.get(newList.size()-1).compareTo(oldList.get(oldList.size()-1)) == 1){
            result = 3;
        }else if(compareForecastLists(oldList, newList)){
            result = 4;
        }

        return result;
    }

    /**
     * This method takes two ArrayLists and checks for Forecast-dates present in newList that are NOT present in oldList
     * @param oldList list of the previous Forecast-entries
     * @param newList list of the newly generated Forecast-entries
     * @return boolean based on the outcome, true if new events, false if not
     */
    //todo: this needs to be implemented properly
    private boolean compareForecastLists(ArrayList<Forecast> oldList, ArrayList<Forecast> newList){
        int[] oldInt = new int[31];
        int[] newInt = new int[31];
        boolean ok = false;
        /*for(Forecast temp : newList){
            for (Forecast temp2 : oldList){
                if (temp2.getStrippedDate(temp2.getFormatedDate())!=temp.getStrippedDate(temp.getFormatedDate())){
                    ok = true;
                }
            }

        }*/
        return ok;
    }

    /**getTimeAndStoreIt is used for updating the last time an AlertSetting had its Forecast-set updated
     *
     * @param current Locale based on the users preferred language
     * @return String containing the current system time, formated.
     */
    public String getTimeAndStoreIt(Locale current){
        Calendar now = Calendar.getInstance(current);
        Date date = now.getTime();

        /*String lastUpdate = date.toString();
        lastUpdate = fixDate(lastUpdate);*/
        String lastUpdate = fixDate(date, current);
        //String lastUpdate = date.getDay() + "/" + date.getMonth()+": " + date.getHours() +"." + date.getMinutes();
        alertSettingsObj.setLastUpdate(lastUpdate);
        alertSettingsRepo.updateAlertsettingsNewLastUpdate(alertSettingsObj.getId(), lastUpdate);
        return lastUpdate;
    }

    /**checkTemp compares the value from a TabularInfo-object and the user-specified value stored in AlertSetting
     *
     * @param value temperature value
     * @return outcome, 0-2 based on the result, 0= match, 1=ignore, 2=no match
     */
    private int checkTemp(double value){

        if ((((double)alertSettingsObj.getTempMin())<value)&&(value<((double)alertSettingsObj.getTempMax()))) {
            //Log.d(TAG, "Temp returverdi = 0, innverdi = " + value);
            tempCheck = true;
            return 0;
        }
        if(alertSettingsObj.getTempMin() == -274){
            //Log.d(TAG, "Temp returverdi = 1, innverdi = " + value);
            return 1;
        }
        //Log.d(TAG, "Temp returverdi = 2, innverdi = " + value);
        return 2;
    }
    /**checkPrecipitation compares the value from a TabularInfo-object and the user-specified value stored in AlertSetting
     *
     * @param value precipitation value
     * @return outcome, 0-2 based on the result, 0= match, 1=ignore, 2=no match
     */
    private int checkPrecipitation(double value) {
        if ((alertSettingsObj.getPrecipitationMin() <= value) && (value < alertSettingsObj.getPrecipitationMax())){
            //Log.d(TAG, "Nedbør returverdi = 0, innverdi = " + value);
            precipitationCheck = true;
            return 0;
        }
        if(alertSettingsObj.getPrecipitationMax() == -1){
            //Log.d(TAG, "Nedbør returverdi = 1, innverdi = " + value);
            return 1;
        }
        //Log.d(TAG, "Nedbør returverdi = 2, innverdi = " + value);
        return 2;
    }
    /**checkWindSpeec compares the value from a TabularInfo-object and the user-specified value stored in AlertSetting
     *
     * @param value windpeed value
     * @return outcome, 0-2 based on the result, 0= match, 1=ignore, 2=no match
     */
    private int checkWindSpeed(double value){
        if ((alertSettingsObj.getWindSpeedMin()<= value)&&(value<alertSettingsObj.getWindSpeedMax())) {
            //Log.d(TAG, "Vindstyrke returverdi = 0, innverdi = " + value);
            windSpeedCheck = true;
            return 0;
        }
        if(alertSettingsObj.getWindSpeedMin() == -1) {
            //Log.d(TAG, "Vindstyrke returverdi = 1, innverdi = " + value);
            return 1;
        }
        //Log.d(TAG, "Vindstyrke returverdi = 2, innverdi = " + value);
        return 2;
    }

    /**checkWindDirection compares the value from a TabularInfo-object and the user-specified value stored in AlertSetting
     *
     * @param a temperature value
     * @return outcome, 0-2 based on the result, 0= match, 1=ignore, 2=no match
     */
    private int checkWindDirection(String a) {

            
        if (alertSettingsObj.getWindDirection() == null){
            Log.d(TAG, "Vindretning returverdi = 1, innverdi = " + a);

            return 1;
        }
        Log.d(TAG, alertSettingsObj.getWindDirection());
        String[] div = alertSettingsObj.getWindDirection().split(", ");
        for (int i = 0; i<div.length; i++){
            if (div[i].equalsIgnoreCase(a)) {
                Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                windDirectionCheck = true;
                return 0;
            }
            /**This switch is required due to the winddirection-resolution provided by yr.no and our choice to lower it for our users.
             * By our definition, adjacent wind-directions are ok, ex: North is set by user, North, North-NorthEast and North-NorthWest are accepted as equivalent.
             */
            if (a.equalsIgnoreCase("NNW") || a.equalsIgnoreCase("WNW") || a.equalsIgnoreCase("WSW") || a.equalsIgnoreCase("SSW") || a.equalsIgnoreCase("SSE") || a.equalsIgnoreCase("ESE") || a.equalsIgnoreCase("NNE") || a.equalsIgnoreCase("ENE")){
                switch (a){
                    case "NNW":
                        if (div[i].equalsIgnoreCase("NW") || div[i].equalsIgnoreCase("N")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            windDirectionCheck = true;
                            return 0;
                        }
                        break;
                    case  "WNW":
                        if (div[i].equalsIgnoreCase("W") || div[i].equalsIgnoreCase("NW")) {
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            windDirectionCheck = true;
                            return 0;
                    }
                        break;
                    case "WSW":
                        if (div[i].equalsIgnoreCase("W") || div[i].equalsIgnoreCase("SW")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            windDirectionCheck = true;
                            return 0;
                        }
                        break;
                    case "SSW":
                        if (div[i].equalsIgnoreCase("S") || div[i].equalsIgnoreCase("SW")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            windDirectionCheck = true;
                            return 0;
                        }
                        break;
                    case "SSE":
                        if (div[i].equalsIgnoreCase("S") || div[i].equalsIgnoreCase("SE")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            windDirectionCheck = true;
                            return 0;
                        }
                        break;
                    case "ESE":
                        if (div[i].equalsIgnoreCase("E") || div[i].equalsIgnoreCase("SE")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            windDirectionCheck = true;
                            return 0;
                        }
                        break;
                    case "NNE":
                        if (div[i].equalsIgnoreCase("N") || div[i].equalsIgnoreCase("NE")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            windDirectionCheck = true;
                            return 0;
                        }
                        break;
                    case "ENE":
                        if (div[i].equalsIgnoreCase("E") || div[i].equalsIgnoreCase("NE")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            windDirectionCheck = true;
                            return 0;
                        }
                        break;
            }
            }

        }

        Log.d(TAG, "Vindretning returverdi = 2, innverdi = " + a);
        return 2;
    }
    /**checkSymbolSun compares the value from a TabularInfo-object and the user-specified value stored in AlertSetting
     *
     * @param a weatherIcon value
     * @return outcome, 0-2 based on the result, 0= match, 1=ignore, 2=no match
     */
    private int checkSymbolSun(String a) {
        //Log.d(TAG, "innhold i SunString: " + a);

        if (alertSettingsObj.isCheckSun() && a.equalsIgnoreCase("clear sky")){
            //Log.d(TAG, "Sol returverdi = 0, innverdi = " + a);
            sunCheck = true;
            return 0;
        }
        if (!alertSettingsObj.isCheckSun()){
            //Log.d(TAG, "sol returverdi 1, innverdi = " +a);
            return 1;
        }
            //Log.d(TAG, "Sol returverdi = 2, innverdi = " + a);
            return 2;


    }
}
