package com.eim.winder.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsDSService;
import com.eim.winder.db.LocationDAO;
import com.eim.winder.db.LocationDSService;
import com.eim.winder.scheduler.AlarmReceiver;
import com.eim.winder.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Mari on 31.01.2016.
 */
public class AlertSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AlertSettingsActivity";

    public LocationDSService datasource;
    public AlertSettingsDSService alertdatasource;
    public AutoCompleteTextView searchView;
    public ArrayAdapter<LocationDAO> searchAdapter;
    public ArrayList<LocationDAO> searchLocations;
    public NumberPicker tempMaxPicker;
    private NumberPicker tempMinPicker;
    private CheckBox checkSun;
    private ArrayAdapter<String> tempAdapter;
    private  String[] tempIntervall;
    private Spinner percipitationSpinner;
    private ArrayAdapter<String> percipitationAdapter;
    private Spinner windspeedSpinner;
    private ArrayAdapter<String> windspeedAdapter;
    private Spinner checkintervalSpinner;
    private ArrayAdapter<String> checkintervalAdapter;
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
        //searchView.addTextChangedListener(new CustomTextChangedListener(this));
        searchAdapter = new ArrayAdapter<LocationDAO>(this, android.R.layout.simple_dropdown_item_1line, searchLocations);
        searchView.setAdapter(searchAdapter);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                locationSelected = (LocationDAO) arg0.getItemAtPosition(position);
                Log.d("############", locationSelected.getId()+ " "+ locationSelected.toString());
            }
        });

    }
    //Initiates all view components in alertsettings_layout when the user has chosen an location for alert
    public void initiateViewComponents(){
        tempIntervall = getResources().getStringArray(R.array.temp_array);
        tempMinPicker = (NumberPicker) findViewById(R.id.tempMin_spinner);
        tempMinPicker.setMinValue(0);
        tempMinPicker.setMaxValue(tempIntervall.length - 1);
        tempMinPicker.setValue((tempIntervall.length - 1) / 2);
        tempMinPicker.setDisplayedValues(tempIntervall);

        tempMaxPicker = (NumberPicker) findViewById(R.id.tempMax_spinner);
        tempMaxPicker.setMinValue(0);
        tempMaxPicker.setMaxValue(tempIntervall.length - 1);
        tempMaxPicker.setValue((tempIntervall.length - 1) / 2);
        tempMaxPicker.setDisplayedValues(tempIntervall);

        percipitationSpinner = (Spinner) findViewById(R.id.nedboer_spinner);
        percipitationAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.percipitation_array));
        percipitationSpinner.setAdapter(percipitationAdapter);

        windspeedSpinner = (Spinner) findViewById(R.id.vindstyrke_spinner);
        windspeedAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.windspeed_array));
        windspeedSpinner.setAdapter(windspeedAdapter);

        checkintervalSpinner = (Spinner) findViewById(R.id.sjekkintervall_spinner);
        checkintervalAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.checkInterval_array));
        checkintervalSpinner.setAdapter(checkintervalAdapter);

        checkSun = (CheckBox) findViewById(R.id.solvarsel_checkBox);
    }
    public void onNextButtonClick(View v){
        if(locationSelected != null && searchView.getText().toString().equals(locationSelected.toString())){
            setContentView(R.layout.alertsettings_layout);
            getSupportActionBar().setTitle(R.string.settings_for_alert);
            initiateViewComponents();
        }else{
            Toast.makeText(this, "Fyll inn et korrekt stedsnavn!", Toast.LENGTH_LONG).show();
        }
    }
    public void onCancelButtonClick(View v){
        finish();
    }
    public void onSaveButtonClick(View v) {
        AlertSettingsDAO asd = makeObjFromSettings();
        interval = asd.getCheckInterval();
        boolean ok = saveAlertSettings(asd);
        if(!ok){
            Toast.makeText(this, "Noe gikk galt..", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Lagret!", Toast.LENGTH_LONG).show();
            ArrayList<AlertSettingsDAO> test;
            test = alertdatasource.getAllAlertSettings();
            for(int i = 0; i < test.size(); i++){
                Log.i("TEST",  "Indeks: " + i + " " + test.get(i).getCheckInterval() + " " + (int) test.get(i).getLocation().getId());
            }
            //Finishes the Activity and return to MainActivity
            finish();
        }

    }

    public boolean saveAlertSettings(AlertSettingsDAO asd){
        Log.i(TAG, "saveAlertSettings()");
        asd.setLocation(locationSelected);

        //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

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
    public AlertSettingsDAO makeObjFromSettings(){
        AlertSettingsDAO asd = new AlertSettingsDAO();
        //Temperature:
        int tempMin = tempMinPicker.getValue();
        int tempMax = tempMaxPicker.getValue();
        if(tempMin != tempMax){
            String[] temp = getResources().getStringArray(R.array.temp_array);
            asd.setTempMin(Integer.parseInt(temp[tempMin]));
            asd.setTempMax(Integer.parseInt(temp[tempMax]));
            Log.i(TAG, "Temp: Min: " + tempMin + " Max: " + tempMax);
        }
        //Rain:
        String rain = percipitationSpinner.getSelectedItem().toString();
        String[] rainTab = rain.split("-");
        if(rainTab.length ==2 && !rain.equals("Not specified(mm)")){
            asd.setPrecipitationMin(Double.parseDouble(rainTab[0].toString()));
            asd.setPrecipitationMax(Double.parseDouble(rainTab[1]));
            Log.i(TAG, "Regn: Min: " + rainTab[0] + " Max: " + rainTab[1]);
        }else if(rainTab[0].equals("More then 9.0")){
            asd.setPrecipitationMin(9);
            asd.setPrecipitationMax(50);
            Log.i(TAG, "Regn: Min: " + 9 + " Max: " + 50);
        }
        //Wind:
        String wind = windspeedSpinner.getSelectedItem().toString();
        String[] windTab = wind.split("-");
        if(windTab.length ==2 && !wind.equals("Not specified (m/s)")){
            asd.setWindSpeedMin(Double.parseDouble(windTab[0]));
            asd.setWindSpeedMax(Double.parseDouble(windTab[1]));
            Log.i(TAG, "Vind: Min: " + windTab[0] + " Max: " + windTab[1]);
        }else if(windTab[0].equals("More then 12")){
            asd.setWindSpeedMin(9);
            asd.setWindSpeedMax(50);
            Log.i(TAG, "Vind: Min: " + 9 + " Max: " + 50);
        }
        //Check interval:
        String interval = checkintervalSpinner.getSelectedItem().toString();
        asd.setCheckInterval(Double.parseDouble(interval));
        asd.setCheckSun(checkSun.isChecked());
        return asd;
    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        super.finish();
    }
    /* this function is used in CustomAutoCompleteTextChangedListener.java
    public String[] getSearchedItemsFromDb(String searchTerm) {
        Log.i(TAG, "getSearchedItemsFromDb()");
        // add items on the array:
        List<LocationDAO> searchRes = datasource.readSearch(searchTerm);
        String[] res = new String[searchRes.size()];
        for (int i = 0; i < searchRes.size(); i++) {
            res[i] = searchRes.get(i).toString();
        }
        return res;
    }*/

    public void scheduleAlarm(int id, double interval){
        long intervalLong = (long)interval*360000;
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






