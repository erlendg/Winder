package com.eim.winder.scheduler;



import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.AlertSettingsRepo;
import com.eim.winder.db.DBService;
import com.eim.winder.db.LocationRepo;
import com.eim.winder.xml.CompareAXService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Erlend on 04.04.2016.
 *
 * AlarmReceiver receives and handles the scheduled alarms, by listening for systemwide broadcasts issued by AlarmManager.
 */
public class AlarmReceiver extends BroadcastReceiver{
    private final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent){
        /**
         * this is where the code to run CompareAXService is implemented.
         */
        //Extracting the integer id from intent:
        Integer id = intent.getIntExtra("id", -1);
        String url = intent.getStringExtra("url");
        int compareResult;
        //Finding the alertsettings-object  based on the Id contained in the received intent:
        DBService dbService = new DBService(context);

        AlertSettings settings = dbService.getCompleteAlertSettingsById(id);

        CompareAXService compare = new CompareAXService(context, settings, url);

        //run the xml-parser:
        boolean div = compare.runHandleXML();

        //if the parsing is done, run findAllOccurences:
        if(div) {
            //creating the NotificationManager needed to display notifications:
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //run the comparison logic:
            compareResult = compare.findAllOccurences(settings.getId(), settings.getLocation().getName(), context, MainActivity.class, mNotificationManager);
            //Update listview if the user is inside the MainActivity:
            //isForeground("com.eim.winder.activities.main.MainActivity", context)
            String lastUpdate = compare.getTimeAndStoreIt(context.getResources().getConfiguration().locale);
            if(MainActivity.getIsActivityRunning()){
               updateAlertSettingsIcon(compareResult, settings);
            }
        }
        //Toast.makeText(context, "Alertsetting " + url, Toast.LENGTH_SHORT).show();

        Log.e(TAG, "onReceive, id: " + id);
        //Toast.makeText(context, "sup? " + intent.getStringExtra("div"), Toast.LENGTH_LONG).show();
    }

    private void updateAlertSettingsIcon(int compareResult, AlertSettings settings){
        Log.i(TAG, "MainActivity in foreground");
        MainActivity activity = MainActivity.getInstance();
        ArrayList<AlertSettings> alerts = activity.getRecycleViewDataset();
        if(compareResult == 1 || compareResult == 3){
            for (int i = 0; i < alerts.size(); i++){
                if(alerts.get(i).getId() == settings.getId()) {
                    activity.notifyAlertSettingsListChanged(i, 1, settings.getLastUpdate());
                    return;
                }
            }
        }if(compareResult == 4){
            for (int i = 0; i < alerts.size(); i++){
                if(alerts.get(i).getId() == settings.getId()) {
                    activity.notifyAlertSettingsListChanged(i, 0, settings.getLastUpdate());
                    return;
                }
            }
        }
    }
}
