package com.eim.winder.scheduler;

import android.app.AlarmManager;
import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

/**
 * Created by Erlend on 17.03.2016.
 */
public class SchedulerWakefulntentService extends WakefulIntentService {

    public SchedulerWakefulntentService(){
        super("Scheduler");

    }
    @Override
    protected void doWakefulWork(Intent intent) {
        Log.i("SchedulerWIS", "doing work");
    }
}
