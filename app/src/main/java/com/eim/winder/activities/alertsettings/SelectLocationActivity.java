package com.eim.winder.activities.alertsettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettingsDSService;
import com.eim.winder.db.LocationDAO;
import com.eim.winder.db.LocationDSService;

import java.util.ArrayList;

/**
 * Created by Mari on 10.04.2016.
 */
public class SelectLocationActivity extends AppCompatActivity {
    private final static String TAG = "SelectLocationActivity";
    private LocationDSService datasource;
    private ArrayList<LocationDAO> searchLocations;
    private AutoCompleteTextView searchView;
    private ArrayAdapter<LocationDAO> searchAdapter;
    private LocationDAO locationSelected;
    private ImageButton sunButton;
    private ImageButton skiButton;
    private ImageButton snowButton;
    private ImageButton hikingButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);
        getSupportActionBar().setTitle(R.string.choose_location);

        // instantiate database handler
        datasource = new LocationDSService(this);
        searchLocations = datasource.getAllLocations();

        // autocompletetextview is in location_layout.xml
        searchView = (AutoCompleteTextView) findViewById(R.id.search_view);
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, searchLocations);
        searchView.setAdapter(searchAdapter);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                locationSelected = (LocationDAO) arg0.getItemAtPosition(position);
                Log.d("############", locationSelected.getId() + " " + locationSelected.toString());
            }
        });

        sunButton = (ImageButton) findViewById(R.id.template_sun);
        skiButton = (ImageButton) findViewById(R.id.template_ski);
        snowButton = (ImageButton) findViewById(R.id.template_snow);
    }
    public void onTemplateButtonClick(View view)
    {
        switch(view.getId()) {
            case R.id.template_sun:
                sunButton.setSelected(!sunButton.isSelected());
                break;

            case R.id.template_ski:
                // Code for button 2 click
                break;

            case R.id.template_hiking:
                // Code for button 3 click
                break;
        }
    }
    public void onNextButtonClick(View v) {
        if (locationSelected != null && searchView.getText().toString().equals(locationSelected.toString())) {
            Intent intent = new Intent(this, AlertSettingsActivityBeta.class);
            intent.putExtra("LocationDAO", locationSelected);
            intent.putExtra("edit", false);
            Log.i(TAG, "---> startAlertSettingsActivity");
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.on_next_button_click_warning), Toast.LENGTH_LONG).show();
        }
    }

    public void onCancelButtonClick(View v) {
        finish();
    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        super.finish();
    }
}
