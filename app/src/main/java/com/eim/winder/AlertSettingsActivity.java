package com.eim.winder;

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

import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsDSService;
import com.eim.winder.db.LocationDAO;
import com.eim.winder.db.LocationDSService;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertsettings_layout);
        // instantiate database handler
        datasource = new LocationDSService(this);
        alertdatasource = new AlertSettingsDSService(this);
        searchLocations = datasource.getAllLocations();
        // autocompletetextview is in alertsettings_layout.xml
        searchView = (AutoCompleteTextView) findViewById(R.id.search_view);
        //searchView.addTextChangedListener(new CustomTextChangedListener(this));
        searchAdapter = new ArrayAdapter<LocationDAO>(this, android.R.layout.simple_dropdown_item_1line, searchLocations);
        searchView.setAdapter(searchAdapter);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                locationSelected = (LocationDAO) arg0.getItemAtPosition(position);
                Log.d("############", locationSelected.toString());
            }
        });
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
        checkintervalAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.checkinterval_array));
        checkintervalSpinner.setAdapter(checkintervalAdapter);

        checkSun = (CheckBox) findViewById(R.id.solvarsel_checkBox);

        /*
        tempMinSpinner = (Spinner) findViewById(R.id.tempMin_spinner);
        tempMaxSpinner = (Spinner) findViewById(R.id.tempMax_spinner);
        tempAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tempIntervall);
        tempMinSpinner.setAdapter(tempAdapter);
        tempMaxSpinner.setAdapter(tempAdapter);*/
        // Add custom text changed listener


    }
    public void onClickSaveButton(View v) {
        AlertSettingsDAO asd = makeObjFromSettings();
        boolean ok = saveAlertSettings(asd);
        if(!ok){
            Toast.makeText(this, "Fyll inn sted!", Toast.LENGTH_LONG).show();
            ArrayList<AlertSettingsDAO> test;
            test = alertdatasource.getAllAlertSettings();
            for(int i = 0; i < test.size(); i++){
                Log.i("TEST",  "Indeks: " + i + " " + test.get(i).getCheckInterval());
            }
        }else {
            Toast.makeText(this, "Lagret!", Toast.LENGTH_LONG).show();
        }

    }
    public boolean saveAlertSettings(AlertSettingsDAO asd){

        boolean ok = false;
        if(locationSelected != null){
            asd.setLocation(locationSelected);
            ok = alertdatasource.insertAlertSettings(asd);
        }
        return ok;
    }
    public AlertSettingsDAO makeObjFromSettings(){
        AlertSettingsDAO asd = new AlertSettingsDAO();
        int tempMin = tempMinPicker.getValue();
        int tempMax = tempMaxPicker.getValue();

        if(tempMin != tempMax){
            String[] temp = getResources().getStringArray(R.array.temp_array);
            asd.setTempMin(Integer.parseInt(temp[tempMin]));
            asd.setTempMax(Integer.parseInt(temp[tempMax]));
            Log.i(TAG, "Temp: Min: " + tempMin + " Max: " + tempMax + " " + temp);
        }
        String regn = percipitationSpinner.getSelectedItem().toString();
        String[] regnTab = regn.split("-");
        if(regnTab.length ==2 && !regn.equals("Not specified(mm)")){
            asd.setPrecipitationMin(Double.parseDouble(regnTab[0].toString()));
            asd.setPrecipitationMax(Double.parseDouble(regnTab[1]));
            Log.i(TAG, "Regn: Min: " + regnTab[0] + " Max: " + regnTab[1]);
        }else if(regnTab[0].equals("More then 9.0")){
            asd.setPrecipitationMin(9);
            asd.setPrecipitationMax(50);
            Log.i(TAG, "Regn: Min: " + 9 + " Max: " + 50);
        }
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
        String interval = checkintervalSpinner.getSelectedItem().toString();
        asd.setCheckInterval(Double.parseDouble(interval));
        asd.setCheckSun(checkSun.isChecked());
        return asd;
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




}






