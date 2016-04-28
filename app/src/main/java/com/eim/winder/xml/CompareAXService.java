package com.eim.winder.xml;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    Context context;

    public CompareAXService(Context context, AlertSettings alertSettingsObj){
        this.context = context;
        this.alertSettingsObj = alertSettingsObj;
        this.forecast = new ForecastInfo();
        this.url = alertSettingsObj.getLocation().getXmlURL();
        this.forecastRepo = new ForecastRepo(context);
        this.alertSettingsRepo = new AlertSettingsRepo(context);


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
     * @param type indicates which kind of notification should be issued.
     */
    public void generateNotification(int i, String locName, Context context, Class cl, NotificationManager nm, int type){
                notification = new NotificationCompat.Builder(context);
                notification.setSmallIcon(R.drawable.ic_stat_name);
                notification.setColor(ContextCompat.getColor(context, R.color.colorPrimary));

                if (type == 1){
                    notification.setContentTitle(context.getResources().getString(R.string.notification_message_title_success));
                    notification.setContentText(context.getResources().getString(R.string.notification_message_text) + " " + locName);
                }else if(type == 2){
                    notification.setContentTitle(context.getResources().getString(R.string.notification_message_title_no_success));
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

    /**
     *
     * @param info
     * @return
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
        if(windDirectionCheck){

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
        }       //"/" + fromDate.substring(5,7) + "/" + fromDate.substring(0,4)
        result += ", " + fromDate.substring(8,10) + "/" + fromDate.substring(5,7) + ": " + fromDate.substring(11,13) + "." + fromDate.substring(14,16) + " - " + toDate.substring(11,13) + "." + toDate.substring(14,16) ;

        return result;
    }

    /**isNetworkOnline checks currently available network-options.
     *
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
        //todo: handle sharedpreferences for connection-types. Also handle lack of connection entirely.
        int currentConnection = isNetworkOnline();
        //if no connection is present, return false:
        if (currentConnection==0){
            return false;
        }

        //find settings for mobiledata as a connection:
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean useMobileData = sp.getBoolean("prefUseMobileData", false);
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

    private void updateAlertSettingsEvent(int id, int hasEvents){
            alertSettingsRepo.updateAlertsettingsHasEvents(id, hasEvents);
    }

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
    public int findAllOccurences(int id, String locName, Context context, Class cl, NotificationManager nm){

        ArrayList<TabularInfo> list = forecast.getTabularList();
        ArrayList<Forecast> returnList = new ArrayList<>();
        Forecast temp;
        String[] dateandinfo;
        int result = -1;
        for (int i = 0; i<list.size(); i++){
            sendNotification = findOccurence(list.get(i));

            if (sendNotification){
                dateandinfo = generateInfo(list.get(i));
                temp = new Forecast();
                temp.setAlertSettingId(alertSettingsObj.getId());
                temp.setFormatedDate(dateandinfo[0]);
                temp.setFormatedInfo(dateandinfo[1]);
                temp.setIcon(list.get(i).getSymbolNumber());
                returnList.add(temp);
            }

        }

        if(returnList.isEmpty()){
            updateAlertSettingsEvent(id, 0);
        } else {
            updateAlertSettingsEvent(id, 1);
        }

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
            if (!returnList.isEmpty()){
                //Case 3: New Forecast-entries found from new XML, and previous Forecast-entries are found in the DB
                addNewForecastsToDB(returnList);
                //generateNotification(returnList, id, context, cl, nm, 1);
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

        Log.e(TAG, "resultat fra checkWeekday: " + checkWeekday(div.getFrom()) );

        return true;
    }
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
    //private int tempMin;
    //private int tempMax;
    private int checkTemp(double value){

        if ((double)(alertSettingsObj.getTempMin())<(value)&&(value<(double)(alertSettingsObj.getTempMax()))) {
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
    //private double precipitationMin;
    //private double precipitationMax;
    private int checkPrecipitation(double value) {
        if ((alertSettingsObj.getPrecipitationMin()) < (value) && (value < (alertSettingsObj.getPrecipitationMax()))){
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
    //private double windSpeedMin;
    //private double windSpeedMax;
    private int checkWindSpeed(double value){
        if ((alertSettingsObj.getWindSpeedMin())<(value)&&(value<(alertSettingsObj.getWindSpeedMax()))) {
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

    //private String windDirection;
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
            if (a.equalsIgnoreCase("NNW") || a.equalsIgnoreCase("WNW") || a.equalsIgnoreCase("WSW") || a.equalsIgnoreCase("SSW") || a.equalsIgnoreCase("SSE") || a.equalsIgnoreCase("ESE") || a.equalsIgnoreCase("NNE") || a.equalsIgnoreCase("ENE")){
                switch (a){
                    case "NNW":
                        if (div[i].equalsIgnoreCase("NW") || div[i].equalsIgnoreCase("N")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case  "WNW":
                        if (div[i].equalsIgnoreCase("W") || div[i].equalsIgnoreCase("NW")) {
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                    }
                        break;
                    case "WSW":
                        if (div[i].equalsIgnoreCase("W") || div[i].equalsIgnoreCase("SW")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "SSW":
                        if (div[i].equalsIgnoreCase("S") || div[i].equalsIgnoreCase("SW")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "SSE":
                        if (div[i].equalsIgnoreCase("S") || div[i].equalsIgnoreCase("SE")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "ESE":
                        if (div[i].equalsIgnoreCase("E") || div[i].equalsIgnoreCase("SE")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "NNE":
                        if (div[i].equalsIgnoreCase("N") || div[i].equalsIgnoreCase("NE")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "ENE":
                        if (div[i].equalsIgnoreCase("E") || div[i].equalsIgnoreCase("NE")){
                            Log.d(TAG, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
            }
            }

        }

        Log.d(TAG, "Vindretning returverdi = 2, innverdi = " + a);
        return 2;
    }
    //private boolean checkSun;
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

        //return 0;
    }
    //private double checkInterval;

    //private boolean mon, tue, wed, thu, fri, sat, sun;




}
