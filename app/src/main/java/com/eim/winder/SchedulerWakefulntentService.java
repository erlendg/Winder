package com.eim.winder;

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
        Log.i("Scheduler", "doing work");
    }
}
