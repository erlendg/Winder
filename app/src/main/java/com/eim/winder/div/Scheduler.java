package com.eim.winder.div;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eim.winder.R;
import com.eim.winder.scheduler.AlarmReceiver;

/**
 * Created by Erlend on 04.04.2016.
 */
public class Scheduler {
    private static final String TAG = "Scheduler";

    public Scheduler(){

    }

    public static void scheduleAlarm(Context context, Class cl, AlarmManager alarmManager, int id){
        long interval = 5000L;
        long startTime = 5000L;
        long nowTime = System.currentTimeMillis() + startTime;
        Log.e(TAG, "scheduleAlarm " + nowTime);

        Intent intentAlarm = new Intent(context, cl);
        intentAlarm.putExtra("div", "stuffs");
        intentAlarm.putExtra("number", id);
        //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent toDo = PendingIntent.getBroadcast(context, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nowTime, interval, toDo);

        Toast.makeText(context, "Alarm scheduled for " + startTime/1000 + " sekunder!", Toast.LENGTH_LONG);
    }

    public static void cancelAlarm(Context context, AlarmManager alarmManager, int id){
        Intent intentAlarm = new Intent(context, AlarmReceiver.class);
        //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent toDo = PendingIntent.getBroadcast(context, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(toDo);
    }
}
