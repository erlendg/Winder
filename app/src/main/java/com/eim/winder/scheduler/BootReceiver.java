package com.eim.winder.scheduler;


import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.DBService;

import java.util.ArrayList;


/**
 * Created by Erlend on 03.05.2016.
 */
public class BootReceiver extends BroadcastReceiver {
    private final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        DBService dbService = new DBService(context);
        ArrayList<AlertSettings> allAlertSettings = dbService.getAllAlertSettingsAndLocations();

        for (AlertSettings temp : allAlertSettings)
        Scheduler.scheduleAlarm(context, AlarmReceiver.class, alarmManager, temp.getId(), temp.getLocation().getXmlURL(), temp.getCheckInterval());

    }

}
