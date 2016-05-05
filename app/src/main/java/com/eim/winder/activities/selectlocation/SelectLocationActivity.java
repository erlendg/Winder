package com.eim.winder.activities.selectlocation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.eim.winder.R;
import com.eim.winder.activities.alertsettings.AlertSettingsActivityBeta;
import com.eim.winder.activities.alertsettings.CustomPrecipRangePreference;
import com.eim.winder.db.DBService;
import com.eim.winder.db.Location;
import com.eim.winder.db.LocationRepo;

import java.util.ArrayList;

/**
 * Created by Mari on 10.04.2016.
 */
public class SelectLocationActivity extends AppCompatActivity {
    private final static String TAG = "SelectLocationActivity";
    private static SelectLocationActivity instance;
    private LocationRepo locationDataSource;
    private DBService dbService;
    private ArrayList<Location> searchLocations;
    private AutoCompleteTextView searchView;
    private ArrayAdapter<Location> searchAdapter;
    private Location locationSelected;
    private ImageButton sunButton;
    private ImageButton snowButton;
    private ImageButton windButton;
    private ImageButton hikingButton;
    private ImageButton rainButton;
    private ImageButton cutomButton;
    SharedPreferences defaultSharedPrefs;
    SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);
        getSupportActionBar().setTitle(R.string.choose_location);
        instance = this;
        // instantiate datasource
        locationDataSource = new LocationRepo(this);
        dbService = new DBService(locationDataSource);
        searchLocations = dbService.getAllLocations();

        // autocompletetextview is in location_layout.xml
        searchView = (AutoCompleteTextView) findViewById(R.id.search_view);
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, searchLocations);
        searchView.setAdapter(searchAdapter);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                locationSelected = (Location) arg0.getItemAtPosition(position);
                Log.d("############", locationSelected.getId() + " " + locationSelected.toString());
            }
        });

        sunButton = (ImageButton) findViewById(R.id.template_sun);
        snowButton = (ImageButton) findViewById(R.id.template_snow);
        windButton = (ImageButton) findViewById(R.id.template_wind);
        hikingButton = (ImageButton) findViewById(R.id.template_hiking);
        rainButton = (ImageButton) findViewById(R.id.template_rain);
        cutomButton = (ImageButton) findViewById(R.id.template_custom);
        cutomButton.setSelected(true);
    }
    public void onTemplateButtonClick(View view)
    {
        switch(view.getId()) {
            case R.id.template_sun:
                sunButton.setSelected(true);
                snowButton.setSelected(false);
                windButton.setSelected(false);
                hikingButton.setSelected(false);
                rainButton.setSelected(false);
                cutomButton.setSelected(false);
                break;

            case R.id.template_snow:
                snowButton.setSelected(true);
                sunButton.setSelected(false);
                windButton.setSelected(false);
                hikingButton.setSelected(false);
                rainButton.setSelected(false);
                cutomButton.setSelected(false);
                break;

            case R.id.template_wind:
                windButton.setSelected(true);
                snowButton.setSelected(false);
                sunButton.setSelected(false);
                hikingButton.setSelected(false);
                rainButton.setSelected(false);
                cutomButton.setSelected(false);
                break;
            case R.id.template_hiking:
                hikingButton.setSelected(true);
                snowButton.setSelected(false);
                sunButton.setSelected(false);
                windButton.setSelected(false);
                rainButton.setSelected(false);
                cutomButton.setSelected(false);
                break;
            case R.id.template_rain:
                rainButton.setSelected(true);
                snowButton.setSelected(false);
                sunButton.setSelected(false);
                hikingButton.setSelected(false);
                windButton.setSelected(false);
                cutomButton.setSelected(false);
                break;
            case R.id.template_custom:
                cutomButton.setSelected(true);
                rainButton.setSelected(false);
                snowButton.setSelected(false);
                sunButton.setSelected(false);
                hikingButton.setSelected(false);
                windButton.setSelected(false);
                break;
        }
    }
    public boolean initializeTemplatePreferences(){
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs = getSharedPreferences(getString(R.string.name_of_prefs_saved), this.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPrefs.edit();
        SharedPreferences.Editor editor = defaultSharedPrefs.edit();
        if(sunButton.isSelected()){
            editor.putBoolean(getString(R.string.sunny_pref_key), true);
            editor2.putString(getString(R.string.prefered_icon_key), "ic_sun_filled");
            editor.apply();
            editor2.apply();
            return true;
        }
        if(snowButton.isSelected()){
            editor.putBoolean(getString(R.string.temp_pref_key), true);
            editor2.putInt(getString(R.string.temp_pref_key_max), -1);
            editor2.putInt(getString(R.string.temp_pref_key_min), -50);

            editor.putBoolean(getString(R.string.precip_pref_key), true);
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_min), 2.0);
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_max), 30.0);
            editor2.putString(getString(R.string.prefered_icon_key), "ic_skiing_48");
            editor.apply();
            editor2.apply();
            return true;
        }
        if(windButton.isSelected()){
            editor.putBoolean(getString(R.string.windspeed_pref_key), true);
            editor2.putInt(getString(R.string.windspeed_pref_key_min), 6);
            editor2.putInt(getString(R.string.windspeed_pref_key_max), 40);
            editor2.putString(getString(R.string.prefered_icon_key), "ic_in_sea_filled");
            editor.apply();
            editor2.apply();
            return true;
        }
        if(hikingButton.isSelected()){
            editor.putBoolean(getString(R.string.precip_pref_key), true);
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_min), 0.0);
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_max), 0.0);

            editor.putBoolean(getString(R.string.windspeed_pref_key), true);
            editor2.putInt(getString(R.string.windspeed_pref_key_min), 0);
            editor2.putInt(getString(R.string.windspeed_pref_key_max), 2);
            editor2.putString(getString(R.string.prefered_icon_key), "ic_trekking");

            editor.apply();
            editor2.apply();
            return true;
        }
        if(rainButton.isSelected()){
            editor.putBoolean(getString(R.string.temp_pref_key), true);
            editor2.putInt(getString(R.string.temp_pref_key_max), 50);
            editor2.putInt(getString(R.string.temp_pref_key_min), 1);

            editor.putBoolean(getString(R.string.precip_pref_key), true);
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_min), 0.5);
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_max), 30.0);
            editor2.putString(getString(R.string.prefered_icon_key), "ic_rainy_weather");
            editor.apply();
            editor2.apply();
            return true;
        }
        return false;
    }
    public void onNextButtonClick(View v) {
        if (locationSelected != null && searchView.getText().toString().equals(locationSelected.toString())) {
            Intent intent = new Intent(this, AlertSettingsActivityBeta.class);
            intent.putExtra("Location", locationSelected);
            intent.putExtra("edit", false);
            boolean t = initializeTemplatePreferences();
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
    public void setLocation(Location loc){
        locationSelected = loc;
    }

    /**
     * For testing only.
     * @return
     */
    public static SelectLocationActivity getInstance(){
        return instance;
    }
}
