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
import com.eim.winder.db.ForecastDAO;
import com.eim.winder.db.ForecastDSService;

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
    private AlertSettingsDAO alertSettingsObj;
    private ForecastInfo forecast;
    private boolean onCreateSuccess = false;
    private boolean sendNotification;
    private Calendar c;
    private Date d,d2;
    private final static String tag = "CompareAXService";
    private boolean tempCheck, precipitationCheck, sunCheck, windDirectionCheck, windSpeedCheck;
    String url;
    private NotificationCompat.Builder notification;
    private ForecastDSService forecastDSService;
    Context context;

    public CompareAXService(Context context, AlertSettingsDAO alertSettingsObj){
        this.context = context;
        this.alertSettingsObj = alertSettingsObj;
        this.forecast = new ForecastInfo();
        this.url = alertSettingsObj.getLocation().getXmlURL();
        this.forecastDSService = new ForecastDSService(context);

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
    public CompareAXService(Context context, AlertSettingsDAO alertSettingsObj, String url){
        this.context = context;
        this.alertSettingsObj = alertSettingsObj;
        this.forecast = new ForecastInfo();
        this.url = url;
        this.forecastDSService = new ForecastDSService(context);


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
     * @param context context of activity that called the method.
     * @param cl class of activity
     * @param nm notificationmanager injected from previously mentioned activity
     * @param type indicates which kind of notification should be issued.
     */
    public void generateNotification(ArrayList<ForecastDAO> a, int i, Context context, Class cl, NotificationManager nm, int type){
                notification = new NotificationCompat.Builder(context);
                notification.setSmallIcon(R.drawable.ic_stat_name);
                notification.setColor(context.getResources().getColor(R.color.colorPrimary));

                if (type == 1){
                    notification.setContentTitle(context.getResources().getString(R.string.notification_message_title_success));
                    notification.setContentText(context.getResources().getString(R.string.notification_message_text) + i + "!");
                }else if(type == 2){
                    notification.setContentTitle(context.getResources().getString(R.string.notification_message_title_no_success));
                    notification.setContentText(context.getResources().getString(R.string.notification_message_text) + i + "!!!!!");
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
        returnString += fixDate(info.getFrom(), info.getTo());
        if (tempCheck){
            returnString += context.getResources().getString(R.string.generate_temperature)+ " " + info.getTemperatureValue() + "\u2103\n"; // grader celcius utf8-kode: "\u2103";

        }
        if(sunCheck){
            returnString += context.getResources().getString(R.string.generate_clear)+"\n";
        }
        if(precipitationCheck){
            returnString += context.getResources().getString(R.string.generate_precipitatiion)+ " " + info.getPrecipitationValue() + "mm" + "\n";
        }
        if(windDirectionCheck){
            returnString += context.getResources().getString(R.string.generate_winddirection) + " " + info.getWindDirectionName() + "\n";
        }
        if (windSpeedCheck){
            returnString += context.getResources().getString(R.string.generate_windspeed)+ " " + info.getWindSpeed() + "m/s \n";
        }
        Log.i(tag, returnString);
        return returnString.trim();
    }

    private String fixDate(String fromDate, String toDate){
        String result = "";

        result += fromDate.substring(8,10) + "/" + fromDate.substring(5,7) + "/" + fromDate.substring(0,4) + ": "+ fromDate.substring(11,13) + "." + fromDate.substring(14,16) + " - " + toDate.substring(11,13) + "." + toDate.substring(14,16) ;

        return result;
    }
    public boolean runHandleXML(){
        xmlHandlerObj.fetchXML();
        while(xmlHandlerObj.parsingComplete);
        return true;

    }
    public void addShitToDB(final ArrayList<ForecastDAO> list){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                forecastDSService.insertForecastList(list, alertSettingsObj.getId());
            }
        });
        thread.start();

        boolean ok = false;
        //ok = forecastDSService.insertForecastList(list, alertSettingsObj.getId());
        //return ok;
    }

    public int findAllOccurences(int id, Context context, Class cl, NotificationManager nm){

        ArrayList<TabularInfo> list = forecast.getTabularList();
        ArrayList<ForecastDAO> returnList = new ArrayList<>();
        ForecastDAO temp;
        String dateandinfo;
        int result = -1;
        for (int i = 0; i<list.size(); i++){
            sendNotification = findOccurence(list.get(i));

            if (sendNotification){
                dateandinfo = generateInfo(list.get(i));
                temp = new ForecastDAO();
                temp.setAlertSettingId(alertSettingsObj.getId());
                temp.setFormatedDate(dateandinfo.substring(0,25));
                temp.setFormatedInfo(dateandinfo.substring(25));
                temp.setIcon(list.get(i).getSymbolNumber());
                returnList.add(temp);
            }

        }

        //// TODO: 19.04.2016 this is where code to handle the four different notification-scenarios is implemented:

        if(!forecastDSService.findIfForecastsExistsForAlertSettingsID(id)){
            //Case 1: New Forecast-entries found from new XML, but no previous Forecast-entries are found in the database(DB)
            if(!returnList.isEmpty()){
                Log.e(TAG, "CASE1");
                addShitToDB(returnList);
                generateNotification(returnList, id, context, cl, nm, 1);
                return 1;
            } else{
                //Case 2: No new Forecast-entries found from new XML, and no previous Forecast-entries are found in the DB
              // do nothing atm
                Log.e(TAG, "CASE2");
                return 2;
            }
        } else{
            if (!returnList.isEmpty()){
                //Case 4: New Forecast-entries found from new XML, and previous Forecast-entries are found in the DB
                addShitToDB(returnList);
                //generateNotification(returnList, id, context, cl, nm, 1);
                Log.e(TAG, "CASE3");
                return 3;
            }else{
                //Case 3: No new Forecast-entries found from new XML, and previous Forecast-entries are found in the DB
                forecastDSService.deleteForecastByAlertSettingsID(id);
                generateNotification(returnList, id, context, cl, nm, 2);
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
    public int checkWeekday(String date){
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
            Log.d(TAG, "Vindretning returverdi = 1, innverdi = " + a);

            return 1;
        }
        Log.d(tag, alertSettingsObj.getWindDirection());
        String[] div = alertSettingsObj.getWindDirection().split(", ");
        for (int i = 0; i<div.length; i++){
            if (div[i].equalsIgnoreCase(a)) {
                Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
                windDirectionCheck = true;
                return 0;
            }
            if (a.equalsIgnoreCase("NNW") || a.equalsIgnoreCase("WNW") || a.equalsIgnoreCase("WSW") || a.equalsIgnoreCase("SSW") || a.equalsIgnoreCase("SSE") || a.equalsIgnoreCase("ESE") || a.equalsIgnoreCase("NNE") || a.equalsIgnoreCase("ENE")){
                switch (a){
                    case "NNW":
                        if (div[i].equalsIgnoreCase("NW") || div[i].equalsIgnoreCase("N")){
                            Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case  "WNW":
                        if (div[i].equalsIgnoreCase("W") || div[i].equalsIgnoreCase("NW")) {
                            Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                    }
                        break;
                    case "WSW":
                        if (div[i].equalsIgnoreCase("W") || div[i].equalsIgnoreCase("SW")){
                            Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "SSW":
                        if (div[i].equalsIgnoreCase("S") || div[i].equalsIgnoreCase("SW")){
                            Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "SSE":
                        if (div[i].equalsIgnoreCase("S") || div[i].equalsIgnoreCase("SE")){
                            Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "ESE":
                        if (div[i].equalsIgnoreCase("E") || div[i].equalsIgnoreCase("SE")){
                            Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "NNE":
                        if (div[i].equalsIgnoreCase("N") || div[i].equalsIgnoreCase("NE")){
                            Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
                    case "ENE":
                        if (div[i].equalsIgnoreCase("E") || div[i].equalsIgnoreCase("NE")){
                            Log.d(tag, "Vindretning returverdi = 0, innverdi = " + a);
                            return 0;
                        }
                        break;
            }
            }

        }

        Log.d(tag, "Vindretning returverdi = 2, innverdi = " + a);
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
