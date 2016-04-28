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
import android.support.v7.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.eim.winder.R;
import com.eim.winder.activities.alertsettings.AlertSettingsActivityBeta;
import com.eim.winder.activities.alertsettings.CustomPrecipRangePreference;
import com.eim.winder.databinding.ActivityAlertOverViewBinding;
import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.AlertSettingsRepo;
import com.eim.winder.db.DBService;
import com.eim.winder.db.ForecastRepo;
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
    private AlertSettingsRepo alertDataSource;
    private ForecastRepo forecastDataSource;
    private DBService dbService;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView preferencesTitle;
    private GridLayout preferencesTable;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_over_view);

        //Gets the object that was sent from MainActivity
        Bundle bundle = getIntent().getExtras();
        alertSettings = bundle.getParcelable("AlertSettings");
        location = alertSettings.getLocation();

        //Opens datasource usage:
        alertDataSource = new AlertSettingsRepo(this);
        forecastDataSource = new ForecastRepo(this);
        dbService = new DBService(alertDataSource,forecastDataSource);

        //Enables databinding to the xml-layout:
        ActivityAlertOverViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_alert_over_view);
        //Enables toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Enables back-button:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new EventListFragment(), getString(R.string.eventlist));
        viewPagerAdapter.addFragment(new SettingsFragment(), getString(R.string.settings));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        //Sets the toolbar name to the location name (doesn't work in xml)
        collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(alertSettings.getLocation().getName());
        // Sets the action of the blue floating action button:
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //Sets the onclick-listener to the preference-title field:
        //preferencesTitle = (TextView) findViewById(R.id.preferences_title);
        preferencesTable = (GridLayout) findViewById(R.id.preferences_table);
    }
    //Sets the onclick-listener to the preference-title field:
    public void onDeleteButtonClick(View v){
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (dbService.deleteAlertSettingAndForecasts(alertSettings.getId())) {
                            finish();
                            cancelAlarm(alertSettings.getId());
                            Toast.makeText(AlertOverViewActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AlertOverViewActivity.this, getString(R.string.something_whent_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

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
        PendingIntent toDo = PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(toDo);

    }
    public void onEditAlertButtonClick(View v){
        Intent intent = new Intent(this, AlertSettingsActivityBeta.class);
        intent.putExtra("Location", alertSettings.getLocation());
        intent.putExtra("edit", true);
        intent.putExtra("alertID", alertSettings.getId());
        Log.i(TAG, "---> startAlertSettingsActivity");
        makePreferencesFromObject(alertSettings);
        startActivity(intent);
        finish();
    }
    public void makePreferencesFromObject(AlertSettings asd){
        SharedPreferences defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.name_of_prefs_saved), this.MODE_PRIVATE);
        SharedPreferences.Editor editor = defaultSharedPrefs.edit();
        SharedPreferences.Editor editor2 = sharedPrefs.edit();
        ArrayList<String> dayElements = new ArrayList<>();
        if(asd.getTempMin() != asd.DEFAULT_TEMP ){
            editor.putBoolean(getString(R.string.temp_pref_key), true);
            editor2.putInt(getString(R.string.temp_pref_key_max), asd.getTempMax());
            editor2.putInt(getString(R.string.temp_pref_key_min), asd.getTempMin());
        }if(asd.getPrecipitationMin() != asd.DEFAULT_WIND_AND_PRECIP ){
            editor.putBoolean(getString(R.string.precip_pref_key), true);
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_min), asd.getPrecipitationMin());
            CustomPrecipRangePreference.putDouble(editor2, getString(R.string.precip_pref_key_max), asd.getPrecipitationMax());
        }if(asd.getWindSpeedMin() != asd.DEFAULT_WIND_AND_PRECIP ){
            editor.putBoolean(getString(R.string.windspeed_pref_key), true);
            editor2.putInt(getString(R.string.windspeed_pref_key_min), (int) asd.getWindSpeedMin());
            editor2.putInt(getString(R.string.windspeed_pref_key_max), (int) asd.getWindSpeedMax());
        }
        if(asd.isCheckSun()) editor.putBoolean(getString(R.string.sunny_pref_key), true);

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
        if(asd.getWindDirection()!= null){
            editor.putBoolean(getString(R.string.winddir_pref_key), true);
            editor2.putString(getString(R.string.winddir_select_key), asd.getWindDirection());
            String[] windArray = getResources().getStringArray(R.array.windDirection_array);
            String[] winddir = asd.getWindDirection().split(", ");
            ArrayList<String> result = new ArrayList<>();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left_anim, R.anim.push_out_right_anim);
    }
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
