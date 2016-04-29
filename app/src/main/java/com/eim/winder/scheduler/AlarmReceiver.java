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

/**
 * Created by Erlend on 04.04.2016.
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
        AlertSettingsRepo alertdatasource = new AlertSettingsRepo(context);
        LocationRepo locationdatasource = new LocationRepo(context);
        DBService dbService = new DBService(alertdatasource, locationdatasource);

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
            if(MainActivity.getIsActivityRunning()){
               updateAlertSettingsIcon(compareResult, settings);
            }
        }
        Toast.makeText(context, "Alertsetting " + url, Toast.LENGTH_SHORT).show();

        Log.e(TAG, "onReceive, id: " + id);
        //Toast.makeText(context, "sup? " + intent.getStringExtra("div"), Toast.LENGTH_LONG).show();
    }
    /*private boolean isForeground(String PackageAndClassName, Context context){
        // Get the Activity Manager
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        // Get a list of running tasks, we are only interested in the last one,
        // As getRunningTasks(int num) is deprecated for SDK 23 and higher we need to check what build version the phone has
       if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
           List< ActivityManager.AppTask > task = manager.getAppTasks();
            // Get the info we need for comparison.
           if(task.size() != 0) {
               ComponentName componentInfo = task.get(0).getTaskInfo().topActivity;
               Log.e(TAG, componentInfo.getClassName());
               if (componentInfo.getClassName().equals(PackageAndClassName)) return true;
           }
            // If not then our app is not on the foreground.
            return false;
        } else {
        /**From: http://developer.android.com/reference/android/app/ActivityManager.html
         * getRunningTasks(int) method was deprecated in API level 21.
         * As of LOLLIPOP, this method is no longer available to third party applications: the introduction of document-centric recents means it can leak person information to the caller.
         * For backwards compatibility, it will still return a small subset of its data: at least the caller's own tasks, and possibly some other tasks such as home that are known to not be sensitive.
         * Unfortunately, there is no other methods that offers the same functionality, and therefore it is still used.

            List< ActivityManager.RunningTaskInfo > task = manager.getRunningTasks(1);
            // Get the info we need for comparison.
            ComponentName componentInfo = task.get(0).topActivity;
            if(componentInfo.getClassName().equals(PackageAndClassName)) return true;
            // If not then our app is not on the foreground.
            return false;
        }
    }*/

    private void updateAlertSettingsIcon(int compareResult, AlertSettings settings){
        Log.i(TAG, "MainActivity in foreground");
        MainActivity activity = MainActivity.getInstace();
        ArrayList<AlertSettings> alerts = activity.getRecycleViewDataset();
        if(compareResult == 1 || compareResult == 3){
            for (int i = 0; i < alerts.size(); i++){
                if(alerts.get(i).getId() == settings.getId()) {
                    activity.notifyAlertSettingsListChanged(i, 1);
                    return;
                }
            }
        }if(compareResult == 4){
            for (int i = 0; i < alerts.size(); i++){
                if(alerts.get(i).getId() == settings.getId()) {
                    activity.notifyAlertSettingsListChanged(i, 0);
                    return;
                }
            }
        }
    }
}
