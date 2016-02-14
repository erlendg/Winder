package com.eim.winder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.LocationDAO;
import com.eim.winder.db.LocationDSService;

import java.util.ArrayList;

/**
 * Created by Mari on 31.01.2016.
 */
public class AlertSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AlertSettingsActivity";

    public LocationDSService datasource;
    public CustomAutoCompleteView searchView;
    public ArrayAdapter<LocationDAO> searchAdapter;
    public ArrayList<LocationDAO> searchLocations;
    public NumberPicker tempMaxPicker;
    private NumberPicker tempMinPicker;
    private ArrayAdapter<String> tempAdapter;
    private  String[] tempIntervall;
    private Spinner percipitationSpinner;
    private ArrayAdapter<String> percipitationAdapter;
    private Spinner windspeedSpinner;
    private ArrayAdapter<String> windspeedAdapter;
    private Spinner checkintervalSpinner;
    private ArrayAdapter<String> checkintervalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertsettings_layout);
        // instantiate database handler
        datasource = new LocationDSService(this);
        // autocompletetextview is in alertsettings_layout.xml
        searchView = (CustomAutoCompleteView) findViewById(R.id.search_view);

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
        windspeedSpinner.setAdapter(percipitationAdapter);

        checkintervalSpinner = (Spinner) findViewById(R.id.sjekkintervall_spinner);
        checkintervalAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.checkinterval_array));
        checkintervalSpinner.setAdapter(checkintervalAdapter);

        /*
        tempMinSpinner = (Spinner) findViewById(R.id.tempMin_spinner);
        tempMaxSpinner = (Spinner) findViewById(R.id.tempMax_spinner);
        tempAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tempIntervall);
        tempMinSpinner.setAdapter(tempAdapter);
        tempMaxSpinner.setAdapter(tempAdapter);*/
        // Add custom text changed listener
        searchView.addTextChangedListener(new CustomTextChangedListener(this));
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchLocations);
        searchView.setAdapter(searchAdapter);

    }
    public void onClickSaveButton(View v) {
        Log.i(TAG, "onClickSaveButton()");
        String text = searchView.getText().toString();
        String[] txtsplit = text.split(",|\\)|\\(");
        for (int i = 0; i < txtsplit.length; i++) {
            Log.i(TAG, txtsplit[i]);
        }
        Log.i("Valgt sted:",searchLocations.get(0).toString());
    }
    public boolean saveAlertSettings(){
        return false;
    }
    public AlertSettingsDAO getAllSavedSettings(){
        AlertSettingsDAO asd = new AlertSettingsDAO();

        return null;
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






