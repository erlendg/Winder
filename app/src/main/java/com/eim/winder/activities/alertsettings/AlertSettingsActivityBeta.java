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

    public LocationDSService datasource;
    public AlertSettingsDSService alertdatasource;
    public AutoCompleteTextView searchView;
    public ArrayAdapter<LocationDAO> searchAdapter;
    public ArrayList<LocationDAO> searchLocations;
    public NumberPicker tempMaxPicker;
    SharedPreferences defaultSharedPrefs;
    SharedPreferences sharedPrefs;
    private LocationDAO locationSelected;
    private double interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);
        getSupportActionBar().setTitle(R.string.choose_location);

        // instantiate database handler
        datasource = new LocationDSService(this);
        alertdatasource = new AlertSettingsDSService(this);
        searchLocations = datasource.getAllLocations();

        // autocompletetextview is in location_layout.xml
        searchView = (AutoCompleteTextView) findViewById(R.id.search_view);
        searchAdapter = new ArrayAdapter<LocationDAO>(this, android.R.layout.simple_dropdown_item_1line, searchLocations);
        searchView.setAdapter(searchAdapter);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                locationSelected = (LocationDAO) arg0.getItemAtPosition(position);
                Log.d("############", locationSelected.getId() + " " + locationSelected.toString());
            }
        });

    }

    //Initiates all view components in alertsettings_layout when the user has chosen an location for alert
    public void initiateViewComponents() {
    }

    public void onNextButtonClick(View v) {
        if (locationSelected != null && searchView.getText().toString().equals(locationSelected.toString())) {
            setContentView(R.layout.alertprefsettings_layout);
            getSupportActionBar().setTitle(R.string.settings_for_alert);
            getFragmentManager().beginTransaction().replace(R.id.prefFragment, new AlertSettingsPrefFragment()).commit();
            PreferenceManager.setDefaultValues(this, R.xml.alert_preferences, true);
            clearPreferencesSaved();
            initiateViewComponents();
        } else {
            Toast.makeText(this, "Fyll inn et korrekt stedsnavn!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Deletes or clears all saved preferences:
     */
    public void clearPreferencesSaved() {
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs = getSharedPreferences(getResources().getString(R.string.name_of_prefs_saved), getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = defaultSharedPrefs.edit();
        SharedPreferences.Editor editor2 = sharedPrefs.edit();
        editor.clear();
        editor.commit();
        editor2.clear();
        editor2.commit();
    }


    public void onCancelButtonClick(View v) {
        finish();
    }

    public void onSaveButtonClick(View v) {
        sharedPrefs = getSharedPreferences(getResources().getString(R.string.name_of_prefs_saved), getApplicationContext().MODE_PRIVATE);
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        AlertSettingsDAO asd = makeObjFromSettings();
        interval = asd.getCheckInterval();
        if(asd.getWindDirection()== null || !asd.getWindDirection().equals("NOT VALID")) {
            boolean ok = saveAlertSettings(asd);
            if (!ok) {
                Toast.makeText(this, "Noe gikk galt..", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Lagret!", Toast.LENGTH_LONG).show();
                ArrayList<AlertSettingsDAO> test;
                test = alertdatasource.getAllAlertSettings();
                for (int i = 0; i < test.size(); i++) {
                    Log.i("TEST", "Indeks: " + i + " " + test.get(i).getCheckInterval() + " " + (int) test.get(i).getLocation().getId());
                }
                //Finishes the Activity and return to MainActivity
                finish();
            }
        }else {
            Toast.makeText(this, "Wind direction not chosen", Toast.LENGTH_LONG).show();
        }
    }

    public boolean saveAlertSettings(AlertSettingsDAO asd) {
        Log.i(TAG, "saveAlertSettings()");
        asd.setLocation(locationSelected);
        long isOk = alertdatasource.insertAlertSettings(asd);
        int id = (int) isOk;
        scheduleAlarm(id, interval);

        if(isOk == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public AlertSettingsDAO makeObjFromSettings() {
        AlertSettingsDAO asd = new AlertSettingsDAO();
        //Temperature:
        if (defaultSharedPrefs.getBoolean("tempPref", false)) {
            int tempMin = sharedPrefs.getInt("minTemp", -50);
            int tempMax = sharedPrefs.getInt("maxTemp", 50);
            asd.setTempMin(tempMin);
            asd.setTempMax(tempMax);
            Log.i(TAG, "Temp: Min: " + tempMin + " Max: " + tempMax);
        }
        //Rain:
        if (defaultSharedPrefs.getBoolean("precipPref", false)) {
            double precipMin = CustomPrecipRangePreference.getDouble(sharedPrefs, "minPrecip", 0.0);
            double precipMax = CustomPrecipRangePreference.getDouble(sharedPrefs, "maxPrecip", 30.0);
            asd.setPrecipitationMin(precipMin);
            asd.setPrecipitationMax(precipMax);
            Log.i(TAG, "Rain: Min: " + precipMin + " Max: " + precipMax);
        }
        //Wind:
        if (defaultSharedPrefs.getBoolean("windSpeedPref", false)) {
            int windSpeedMin = sharedPrefs.getInt("minWindSpeed", 0);
            int windSpeedMax = sharedPrefs.getInt("maxWindSpeed", 40);
            asd.setWindSpeedMin(windSpeedMin);
            asd.setWindSpeedMax(windSpeedMax);
            Log.i(TAG, "Wind: Min: " + windSpeedMin + " Max: " + windSpeedMax);
        }
        if (defaultSharedPrefs.getBoolean("windDirPref", false)) {
            String windDirections = sharedPrefs.getString("windDir", "NOT VALID");
            asd.setWindDirection(windDirections);
            Log.i(TAG, "WindDir: " + windDirections);
        }
        //Weekdays:
        Set<String> weekdaysSet = sharedPrefs.getStringSet("weekdays", null);
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
        String interval = sharedPrefs.getString("checkIntrPref", "Every 6 hour");
        String split[] = interval.split(" ");
        Log.i(TAG, "CheckInterval: " + interval);
        asd.setCheckInterval(Double.parseDouble(split[1]));
        //Sun:
        asd.setCheckSun(defaultSharedPrefs.getBoolean("sunnyPref", false));
        return asd;

    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        super.finish();
    }

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



