package com.eim.winder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.commonsware.cwac.wakeful.WakefulIntentService;

/**
 * Created by Erlend on 17.03.2016.
 */
public class SchedulerAlarmListener implements WakefulIntentService.AlarmListener {

    public SchedulerAlarmListener(){

    }

    @Override
    public void scheduleAlarms(AlarmManager alarmManager, PendingIntent pendingIntent, Context context) {

    }

    @Override
    public void sendWakefulWork(Context context) {

    }

    @Override
    public long getMaxAge() {
        return 0;
    }
}
