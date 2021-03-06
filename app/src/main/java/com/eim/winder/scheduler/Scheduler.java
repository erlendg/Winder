package com.eim.winder.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.GregorianCalendar;

/**
 * Created by Erlend on 04.04.2016.
 *
 * Static class for alarm scheduling.
 */
public class Scheduler {
    private static final String TAG = "Scheduler";

    public Scheduler(){

    }

    public static void scheduleAlarm(Context context, Class cl, AlarmManager alarmManager, int id, String url, double checkInterval){
        long intervalLong = (long)checkInterval*3600000; //  3600000 = millisekund i timen
        long startTime = 5000;
        long nowTime = new GregorianCalendar().getTimeInMillis() + startTime;
        Log.e(TAG, "scheduleAlarm: " + id);
        Intent intentAlarm = new Intent(context, AlarmReceiver.class);
        intentAlarm.putExtra("id", id);
        intentAlarm.putExtra("url", url);

        //AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nowTime, intervalLong, pendingIntent);

        //Toast.makeText(context, "Alarm scheduled for " + id + "!", Toast.LENGTH_SHORT).show();
    }

    public static void cancelAlarm(Context context, AlarmManager alarmManager, int id){
        Intent intentAlarm = new Intent(context, AlarmReceiver.class);
        //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent toDo = PendingIntent.getBroadcast(context, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(toDo);
    }
}
