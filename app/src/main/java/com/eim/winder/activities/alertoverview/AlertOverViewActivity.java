package com.eim.winder.activities.alertoverview;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.eim.winder.R;
import com.eim.winder.activities.alertsettings.AlertSettingsActivity;
import com.eim.winder.activities.alertsettings.CustomPrecipRangePreference;
import com.eim.winder.databinding.ActivityAlertOverViewBinding;
import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.DBService;
import com.eim.winder.db.Location;
import com.eim.winder.scheduler.AlarmReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AlertOverViewActivity extends AppCompatActivity {
    private final String TAG = "AlertOverWievActivity";
    private AlertSettings alertSettings;
    private Location location;
    private DBService dbService;
    private CollapsingToolbarLayout collapsingToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    /**
     * Creates the detailed view of the AlertSettings (AlertOverViewActivity)
     * initiates the view, database service (DBService), databinding of an AlertSettings object in xml file,
     * toolbar, view components and tabs.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_over_view);

        //Gets the object that was sent from MainActivity
        Bundle bundle = getIntent().getExtras();
        alertSettings = bundle.getParcelable("AlertSettings");
        location = alertSettings.getLocation();

        //Opens datasource usage:
        dbService = new DBService(this);

        //Enables databinding to the xml-layout:
        ActivityAlertOverViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_alert_over_view);

        buildViewComponents();
    }
    private void buildViewComponents(){
        //Enables toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Enables back-button:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initiates the tabs which inflates the EventListFragment and SettingsFragment trough ViewPagerAdapter:
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new EventListFragment(), getString(R.string.eventlist));
        viewPagerAdapter.addFragment(new SettingsFragment(), getString(R.string.settings));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //Sets the toolbar name to the location name:
        collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(alertSettings.getLocation().getName());
    }

    /**
     * Deletes the selected AlertSettings object from database
     * @param view the view of the delete button: activity_alert_over_view.xml
     */
    public void onDeleteButtonClick(View view){
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (dbService.deleteAlertSettingAndForecasts(alertSettings.getId())) {
                            cancelAlarm(alertSettings.getId());
                            finish();
                            overridePendingTransition(R.anim.pull_in_left_anim, R.anim.push_out_right_anim);
                            Toast.makeText(AlertOverViewActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AlertOverViewActivity.this, getString(R.string.something_whent_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null).show();
    }

    /**
     * For the inflated fragment classes only.
     * @return AlertSettings object.
     */
    public AlertSettings getAlertSettings(){
        return alertSettings;
    }

    /**
     * Method to cancel a currently scheduler AlarmManager alarm based on the original Intent's id.
     * @param id id of the desired AlertSetting, and subsequently Intent.
     */
    public void cancelAlarm(int id){
        Log.e(TAG,"cancelAlarm: "+ id );
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

    }

    /**
     * Starts AlertSettingsActivity if the user wishes to edit the weather settings for the Alert (AlertSettings)
     * @param v view of the edit button:
     */
    public void onEditAlertButtonClick(View view){
        Intent intent = new Intent(this, AlertSettingsActivity.class);
        intent.putExtra("Location", alertSettings.getLocation());
        intent.putExtra("edit", true);
        intent.putExtra("alertID", alertSettings.getId());
        Log.i(TAG, "---> startAlertSettingsActivity");
        makePreferencesFromObject(alertSettings);
        startActivity(intent);
        finish();
    }

    /**
     * Populates DefaultSharedPreferences and SharedPreferences for the edit AlertSettings view:
     * @param asd the AlertSettings object
     */
    public void makePreferencesFromObject(AlertSettings asd){
        SharedPreferences defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.name_of_prefs_saved), this.MODE_PRIVATE);
        SharedPreferences.Editor editor = defaultSharedPrefs.edit();
        SharedPreferences.Editor editor2 = sharedPrefs.edit();
        ArrayList<String> dayElements = new ArrayList<>();
        //Temperature:
        if(asd.getTempMin() != asd.DEFAULT_TEMP ){
            editor.putBoolean(getString(R.string.temp_pref_key), true);
            editor2.putInt(getString(R.string.temp_pref_key_max), asd.getTempMax());
            editor2.putInt(getString(R.string.temp_pref_key_min), asd.getTempMin());
            //Precipitation:
        }if(asd.getPrecipitationMin() != asd.DEFAULT_WIND_AND_PRECIP ){
            editor.putBoolean(getString(R.string.precip_pref_key), true);
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_min), asd.getPrecipitationMin());
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_max), asd.getPrecipitationMax());
            // Wind Speed:
        }if(asd.getWindSpeedMin() != asd.DEFAULT_WIND_AND_PRECIP ){
            editor.putBoolean(getString(R.string.windspeed_pref_key), true);
            editor2.putInt(getString(R.string.windspeed_pref_key_min), (int) asd.getWindSpeedMin());
            editor2.putInt(getString(R.string.windspeed_pref_key_max), (int) asd.getWindSpeedMax());
        }
        //Sun:
        if(asd.isCheckSun()) editor.putBoolean(getString(R.string.sunny_pref_key), true);
        //Weekdays:
        if(asd.isMon()) dayElements.add("0");
        if(asd.isTue()) dayElements.add("1");
        if(asd.isWed()) dayElements.add("2");
        if(asd.isThu()) dayElements.add("3");
        if(asd.isFri()) dayElements.add("4");
        if(asd.isSat()) dayElements.add("5");
        if(asd.isSun()) dayElements.add("6");
        if(!dayElements.isEmpty()){
            String saveElements[] = dayElements.toArray(new String[dayElements.size()]);
            Set<String> days = new HashSet<>(Arrays.asList(saveElements));
            editor.putStringSet(getString(R.string.weekdays_pref_key), days);
        }
        // Wind direction:
        if(asd.getWindDirection()!= null){
            editor.putBoolean(getString(R.string.winddir_pref_key), true);
            editor2.putString(getString(R.string.winddir_select_key), asd.getWindDirection());
            String[] windArray = getResources().getStringArray(R.array.windDirection_array);
            String[] winddir = asd.getWindDirection().split(", ");
            ArrayList<String> result = new ArrayList<>();
            // Since it is stored as a string it needs to be split and then stored inside a new string set for the Preferences:
            for(int i = 0; i < winddir.length; i++){
             for(int j = 0; j <windArray.length; j++){
                 //Log.i("INDEX", "winddir i: "+i+ " windarray j: " + j +" winddirl" + winddir.length);
                 if(winddir[i].equals(windArray[j])) result.add(""+j);
             }
            }
            Log.i(TAG,"makePreferencesFromObject() wind: " + winddir.toString());
            String saveElements[] = result.toArray(new String[result.size()]);
            Set<String> days = new HashSet<>(Arrays.asList(saveElements));
            editor.putStringSet(getString(R.string.winddir_select_key), days);
        }
        //Check interval:
        String[] checkintr = getResources().getStringArray(R.array.windDirection_array);
        if(asd.getCheckInterval() == 1.0)editor.putString(getString(R.string.checkintr_pref_key), ""+0);
        if(asd.getCheckInterval() == 2.0)editor.putString(getString(R.string.checkintr_pref_key), ""+1);
        if(asd.getCheckInterval() == 4.0)editor.putString(getString(R.string.checkintr_pref_key),""+2);
        if(asd.getCheckInterval() == 6.0)editor.putString(getString(R.string.checkintr_pref_key),""+3);
        if(asd.getCheckInterval() == 12.0)editor.putString(getString(R.string.checkintr_pref_key),""+4);
        if(asd.getCheckInterval() == 24.0)editor.putString(getString(R.string.checkintr_pref_key),""+5);
        if(asd.getCheckInterval() == 48.0)editor.putString(getString(R.string.checkintr_pref_key),""+6);
        editor2.putString(getString(R.string.prefered_icon_key), asd.getIconName());
        editor.apply();
        editor2.apply();
    }

    /**
     * Sets a right to left animation if back button is pressed.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left_anim, R.anim.push_out_right_anim);
    }

    /**
     * Builds the menu back/homescreen arrow symbol
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                break;
        }
        return false;
    }

}
