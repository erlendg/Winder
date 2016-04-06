package com.eim.winder.scheduler;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.eim.winder.activities.MainActivity;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsDSService;
import com.eim.winder.xml.CompareAXService;

import java.util.ArrayList;

/**
 * Created by Erlend on 04.04.2016.
 */
public class AlarmReceiver extends BroadcastReceiver{
    private final String TAG = "AlarmReceiver.class";

    @Override
    public void onReceive(Context context, Intent intent){
        //TODO implement executable code
        /**
         * this is where the code to run CompareAXService is implemented.
         */
        //Extracting the integer id from intent:
        Integer id = intent.getIntExtra("id", -1);
        String url = intent.getStringExtra("url");

        //Finding the alertsettings-object  based on the Id contained in the received intent:
        AlertSettingsDSService alertdatasource = new AlertSettingsDSService(context);
        AlertSettingsDAO settings= alertdatasource.getAlertSettingById(id);

        CompareAXService compare = new CompareAXService(settings, url);

        //run the xml-parser:
        boolean div = compare.runHandleXML();

        //if the parsing is done, run findAllOccurences:
        if(div) {
            ArrayList<String> listeTing = compare.findAllOccurences();
            Log.e(TAG, " hei 1");
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Log.e(TAG, "hei 2");
            compare.generateNotification(listeTing, settings.getId(), context, MainActivity.class, mNotificationManager);
            Log.e(TAG, "hei 3");
        }
        Toast.makeText(context, "Alertsetting " + url, Toast.LENGTH_SHORT).show();

        Log.e(TAG, "onReceive, id: " + id);
        //Toast.makeText(context, "sup? " + intent.getStringExtra("div"), Toast.LENGTH_LONG).show();
    }
}
