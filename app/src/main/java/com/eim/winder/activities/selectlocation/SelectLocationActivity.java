package com.eim.winder.activities.selectlocation;

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
    private ImageButton customButton;
    SharedPreferences defaultSharedPrefs;
    SharedPreferences sharedPrefs;

    /**
     * Creates the SelectLocationActivity view. initiate the database service DBService, sets content view
     * builds the AutoCompleteTextView based on all the selectable Locations in the database.
     * Also initiates the template buttons.
     * @param savedInstanceState Activity bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);
        getSupportActionBar().setTitle(R.string.choose_location);
        instance = this;
        // instantiate datasource
        LocationRepo locationDataSource = new LocationRepo(this);
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
        //Initiates template buttons:
        initiateButtons();

    }

    /**
     * Initiates the template buttons and set the custom button as selected.
     */
    public void initiateButtons(){
        sunButton = (ImageButton) findViewById(R.id.template_sun);
        snowButton = (ImageButton) findViewById(R.id.template_snow);
        windButton = (ImageButton) findViewById(R.id.template_wind);
        hikingButton = (ImageButton) findViewById(R.id.template_hiking);
        rainButton = (ImageButton) findViewById(R.id.template_rain);
        customButton = (ImageButton) findViewById(R.id.template_custom);
        customButton.setSelected(true);
    }

    /**
     * Custom button selected listener witch generates a radio button effect so the user only can choose
     * one button at the time.
     * @param view needs the view of the buttons because the click action is initiated in the location_layout.xml-file
     *             in the onClick parameter.
     */
    public void onTemplateButtonClick(View view)
    {
        switch(view.getId()) {
            case R.id.template_sun:
                sunButton.setSelected(true);
                snowButton.setSelected(false);
                windButton.setSelected(false);
                hikingButton.setSelected(false);
                rainButton.setSelected(false);
                customButton.setSelected(false);
                break;

            case R.id.template_snow:
                snowButton.setSelected(true);
                sunButton.setSelected(false);
                windButton.setSelected(false);
                hikingButton.setSelected(false);
                rainButton.setSelected(false);
                customButton.setSelected(false);
                break;

            case R.id.template_wind:
                windButton.setSelected(true);
                snowButton.setSelected(false);
                sunButton.setSelected(false);
                hikingButton.setSelected(false);
                rainButton.setSelected(false);
                customButton.setSelected(false);
                break;
            case R.id.template_hiking:
                hikingButton.setSelected(true);
                snowButton.setSelected(false);
                sunButton.setSelected(false);
                windButton.setSelected(false);
                rainButton.setSelected(false);
                customButton.setSelected(false);
                break;
            case R.id.template_rain:
                rainButton.setSelected(true);
                snowButton.setSelected(false);
                sunButton.setSelected(false);
                hikingButton.setSelected(false);
                windButton.setSelected(false);
                customButton.setSelected(false);
                break;
            case R.id.template_custom:
                customButton.setSelected(true);
                rainButton.setSelected(false);
                snowButton.setSelected(false);
                sunButton.setSelected(false);
                hikingButton.setSelected(false);
                windButton.setSelected(false);
                break;
        }
    }

    /**
     * Initiates or pre-populates the SharedPreferences for the AlertSettings object for the location based on
     * which template button who is selected.
     * @return true if any of the buttons except the custom template button is selected.
     */
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

    /**
     * Starts the AlertSettingsActivityBeta if the user has selected a location from the auto complete textview
     * @param v the view of the next button: location_layout.xml
     */
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

    /**
     * Finish the activity if the user wish to cancel the SelectLocationActivity.
     * @param v the view of the cancel button: location_layout.xml
     */
    public void onCancelButtonClick(View v) {
        finish();
    }

    /**-------------------------- METHODS FOR TESTING ONY ----------------------------
     * For testing only. Sets the selected location
     * @param loc the location to be selected.
     */
    public void setLocation(Location loc){
        locationSelected = loc;
    }

    /**
     * For testing only.
     * @return instance of the SelectLocationActivity
     */
    public static SelectLocationActivity getInstance(){
        return instance;
    }
}
