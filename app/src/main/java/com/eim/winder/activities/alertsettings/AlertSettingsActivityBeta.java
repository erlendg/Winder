package com.eim.winder.activities.alertsettings;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsDSService;
import com.eim.winder.db.LocationDAO;
import com.eim.winder.db.LocationDSService;
import com.eim.winder.scheduler.AlarmReceiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Mari on 05.04.2016.
 */
public class AlertSettingsActivityBeta extends AppCompatActivity {
    private static final String TAG = "ASActivityBeta";

    public AlertSettingsDSService alertdatasource;
    SharedPreferences defaultSharedPrefs;
    SharedPreferences sharedPrefs;
    private LocationDAO locationSelected;
    private boolean haveSelectedSomething = false;
    private double interval;
    private boolean updateMode;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertprefsettings_layout);
        getSupportActionBar().setTitle(R.string.settings_for_alert);
        getFragmentManager().beginTransaction().replace(R.id.prefFragment, new AlertSettingsPrefFragment()).commit();
        //PreferenceManager.setDefaultValues(this, R.xml.alert_preferences, true);
        bundle = getIntent().getExtras();
        locationSelected = bundle.getParcelable("LocationDAO");
        updateMode = bundle.getBoolean("edit");
        // instantiate database handler
        alertdatasource = new AlertSettingsDSService(this);
    }

    /**
     * Deletes or clears all saved preferences from context based on preference name.
     * @param context
     * @param sharedPrefsName
     */
    public void clearPreferencesSaved(Context context,String sharedPrefsName ) {
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs = getSharedPreferences(sharedPrefsName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = defaultSharedPrefs.edit();
        SharedPreferences.Editor editor2 = sharedPrefs.edit();
        editor.clear();
        editor.apply();
        editor2.clear();
        editor2.apply();
    }


    public void onCancelButtonClick(View v) {
        clearPreferencesSaved(this, getString(R.string.name_of_prefs_saved) );
        finish();
    }

    private void updatePreferences(){
        sharedPrefs = getSharedPreferences(getResources().getString(R.string.name_of_prefs_saved), getApplicationContext().MODE_PRIVATE);
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }
    public void onSaveButtonClick(View v) {
        updatePreferences();
        AlertSettingsDAO asd = makeObjFromSettings(defaultSharedPrefs, sharedPrefs);
        if(!haveSelectedSomething){
            Toast.makeText(this, getString(R.string.no_settings_selected), Toast.LENGTH_LONG).show();
            return;
        }
        if(asd.getWindDirection() != null) {
            if (asd.getWindDirection().equals("NOT VALID")) {
                Toast.makeText(this, getString(R.string.wind_dir_not_chosen), Toast.LENGTH_LONG).show();
                return;
            }
        }
        interval = asd.getCheckInterval();
        boolean ok = saveAlertSettings(asd);
        clearPreferencesSaved(this, getString(R.string.name_of_prefs_saved) );
        if (!ok) {
            Toast.makeText(this, "Noe gikk galt..", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Lagret!", Toast.LENGTH_LONG).show();
            /*ArrayList<AlertSettingsDAO> test;
            test = alertdatasource.getAllAlertSettings();
            for (int i = 0; i < test.size(); i++) {
                Log.i("TEST", "Indeks: " + i + " " + test.get(i).getCheckInterval() + " " + (int) test.get(i).getLocation().getId());
            }*/
            //Finishes the Activity and return to MainActivity
            finish();
        }
    }

    public boolean saveAlertSettings(AlertSettingsDAO asd) {
        Log.i(TAG, "saveAlertSettings()");
        long isOk = -1;
        if(updateMode){
            asd.setId(bundle.getInt("alertID"));
            isOk = alertdatasource.updateAlertSettings(asd);
        }else {
            asd.setLocation(locationSelected);
            isOk = alertdatasource.insertAlertSettings(asd);
        }
        if((int)isOk != -1 && (int) isOk != 0) {
            scheduleAlarm((int) isOk, interval);
            return true;
        }
        return false;
    }

    public AlertSettingsDAO makeObjFromSettings(SharedPreferences defaultSharedPrefs, SharedPreferences sharedPrefs) {
        AlertSettingsDAO asd = new AlertSettingsDAO();
        //Temperature:
        if (defaultSharedPrefs.getBoolean(getString(R.string.temp_pref_key), false)) {
            int tempMin = sharedPrefs.getInt(getString(R.string.temp_pref_key_min), -50);
            int tempMax = sharedPrefs.getInt(getString(R.string.temp_pref_key_max), 50);
            asd.setTempMin(tempMin);
            asd.setTempMax(tempMax);
            haveSelectedSomething = true;
            Log.i(TAG, "Temp: Min: " + tempMin + " Max: " + tempMax);
        }
        //Rain:
        if (defaultSharedPrefs.getBoolean(getString(R.string.precip_pref_key), false)) {
            double precipMin = CustomPrecipRangePreference.getDouble(sharedPrefs, getString(R.string.precip_pref_key_min), 0.0);
            double precipMax = CustomPrecipRangePreference.getDouble(sharedPrefs, getString(R.string.precip_pref_key_max), 30.0);
            asd.setPrecipitationMin(precipMin);
            asd.setPrecipitationMax(precipMax);
            haveSelectedSomething = true;
            Log.i(TAG, "Rain: Min: " + precipMin + " Max: " + precipMax);
        }
        //Wind:
        if (defaultSharedPrefs.getBoolean(getString(R.string.windspeed_pref_key), false)) {
            int windSpeedMin = sharedPrefs.getInt(getString(R.string.windspeed_pref_key_min), 0);
            int windSpeedMax = sharedPrefs.getInt(getString(R.string.windspeed_pref_key_max), 40);
            asd.setWindSpeedMin(windSpeedMin);
            asd.setWindSpeedMax(windSpeedMax);
            haveSelectedSomething = true;
            Log.i(TAG, "Wind: Min: " + windSpeedMin + " Max: " + windSpeedMax);
        }
        if (defaultSharedPrefs.getBoolean(getString(R.string.winddir_pref_key), false)) {
            String windDirections = sharedPrefs.getString(getString(R.string.winddir_select_key), "NOT VALID");
            asd.setWindDirection(windDirections);
            haveSelectedSomething = true;
            Log.i(TAG, "WindDir: " + windDirections);
        }
        //Weekdays:
        Set<String> weekdaysSet = sharedPrefs.getStringSet(getString(R.string.weekdays), null);
        if(weekdaysSet == null || weekdaysSet.size() == 0){
            asd.setMon(true); asd.setTue(true); asd.setWed(true); asd.setThu(true); asd.setFri(true); asd.setSat(true); asd.setSun(true);
        }else {
            for(String weekday : weekdaysSet){
                if(weekday.equals("1")) asd.setMon(true);
                if(weekday.equals("2")) asd.setTue(true);
                if(weekday.equals("3")) asd.setWed(true);
                if(weekday.equals("4")) asd.setThu(true);
                if(weekday.equals("5")) asd.setFri(true);
                if(weekday.equals("6")) asd.setSat(true);
                if(weekday.equals("7")) asd.setSun(true);
                Log.i(TAG, "Weekdays: " + weekdaysSet.toString());
            }
        }
        //Check interval:
        String interval = sharedPrefs.getString(getString(R.string.checkintr_pref_key), "Every 6 hour");
        String split[] = interval.split(" ");
        Log.i(TAG, "CheckInterval: " + interval);
        asd.setCheckInterval(Double.parseDouble(split[1]));
        //Sun:
        if(defaultSharedPrefs.getBoolean(getString(R.string.sunny_pref_key), false)){
            asd.setCheckSun(true);
            haveSelectedSomething = true;
        }
        // Icon:
        String icon = sharedPrefs.getString(getString(R.string.prefered_icon_key),getString(R.string.prefered_icon_key_default));
        asd.setIconName(icon);
        return asd;
    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        super.finish();
    }

    /**
     *
     * @param id
     * @param interval
     */
    public void scheduleAlarm(int id, double interval){
        long intervalLong = (long)interval*3600000; //  3600000 = millisekund i timen
        long startTime = 5000;
        long nowTime = new GregorianCalendar().getTimeInMillis() + startTime;
        Log.e(TAG, "scheduleAlarm: " + id);
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        intentAlarm.putExtra("id", id);
        intentAlarm.putExtra("url", locationSelected.getXmlURL() );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent toDo = PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nowTime, intervalLong, toDo);

        Toast.makeText(this, "Alarm scheduled for Id: " + id + "!", Toast.LENGTH_LONG).show();
    }
}




