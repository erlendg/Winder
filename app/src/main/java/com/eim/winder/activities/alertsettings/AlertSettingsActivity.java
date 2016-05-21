package com.eim.winder.activities.alertsettings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.AlertSettingsRepo;
import com.eim.winder.db.DBService;
import com.eim.winder.db.Location;
import com.eim.winder.scheduler.AlarmReceiver;

import java.util.GregorianCalendar;
import java.util.Set;

/**
 * Created by Mari on 05.04.2016.
 */
public class AlertSettingsActivity extends AppCompatActivity {
    private static final String TAG = "ASActivity";

    public AlertSettingsRepo alertDataSource;
    private DBService dbService;
    SharedPreferences defaultSharedPrefs;
    SharedPreferences sharedPrefs;
    private Location locationSelected;
    private boolean haveSelectedSomething = false;
    private boolean updateMode;
    Bundle bundle;

    /**
     * Creates the AlertSettingsActivity content view: alertprefsettings_layout.xml,
     * sets toolbar title, inflate the preferenceFragment containing the settings view,
     * fetches the selected Location form the bundle extras form the previous activity,
     * sets the updateMode value (if the previous activity is the AlertOverViewActivity)
     * and
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertprefsettings_layout);
        getSupportActionBar().setTitle(R.string.settings_for_alert);
        getFragmentManager().beginTransaction().replace(R.id.prefFragment, new AlertSettingsPrefFragment()).commit();
        bundle = getIntent().getExtras();
        locationSelected = bundle.getParcelable("Location");
        updateMode = bundle.getBoolean("edit");
        // instantiate database handler
        alertDataSource = new AlertSettingsRepo(this);
        dbService = new DBService(alertDataSource);
    }

    /**
     * Deletes or clears all saved preferences from context based on preference name.
     * Only holds on the app settings preferences (Mobile data and language) the rest
     * needs to be cleared before next initialization of the view.
     * @param context
     * @param sharedPrefsName Needs name of the shared preference which is not the default to clear it.
     */
    public void clearPreferencesSaved(Context context,String sharedPrefsName ) {
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs = getSharedPreferences(sharedPrefsName, context.MODE_PRIVATE);
        //Needs to still be saved
        String selectedLanguage = defaultSharedPrefs.getString(getString(R.string.language_pref_key), "default");
        boolean useMobileData = defaultSharedPrefs.getBoolean("prefUseMobileData", true);
        SharedPreferences.Editor editor = defaultSharedPrefs.edit();
        SharedPreferences.Editor editor2 = sharedPrefs.edit();
        editor.clear();
        editor.commit();
        editor2.clear();
        editor2.commit();

        //Puts locale and network settings back in shared preferences.
        editor.putString(getString(R.string.language_pref_key), selectedLanguage);
        editor.putBoolean(getString(R.string.mobile_network_pref_key), useMobileData);
        editor.apply();

    }
    /**
     * If canceled, go back to MainActivity.
     * @param v the view of the cancel button
     */
    public void onCancelButtonClick(View v) {
        clearPreferencesSaved(this, getString(R.string.name_of_prefs_saved) );
        finish();
    }
    /**
     * Update the reference to shared preferences.
     */
    private void updatePreferences(){
        sharedPrefs = getSharedPreferences(getResources().getString(R.string.name_of_prefs_saved), getApplicationContext().MODE_PRIVATE);
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }
    /**
     * Gets called when the user clicks on the save button
     * @param view the view of the save button
     */
    public void onSaveButtonClick(View view) {
        updatePreferences();
        //Makes an object out of the SharedPreferences settings:
        AlertSettings asd = makeObjFromPreferences(defaultSharedPrefs, sharedPrefs);
        //If the user did not select something then tell him:
        if(!haveSelectedSomething){
            Snackbar.make(view, getString(R.string.no_settings_selected), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        //If the user has checked the wind direction but has not selected witch wind directions he prefers
        //then tell him:
        if(asd.getWindDirection() != null) {
            if (asd.getWindDirection().equals("NOT VALID")) {
                Snackbar.make(view, getString(R.string.wind_dir_not_chosen), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return;
            }
        }
        //Save the generated object to database:
        boolean ok = saveAlertSettings(asd);
        clearPreferencesSaved(this, getString(R.string.name_of_prefs_saved));
        if (!ok) {
            Snackbar.make(view, getString(R.string.something_whent_wrong), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            Toast.makeText(this, R.string.saved_toast, Toast.LENGTH_LONG).show();
            //Finishes the Activity and return to MainActivity
            finish();
        }
    }

    /**
     * Saves the generated object to the database.
     * If the updateMode is true: update an already exixting object based on the bundle id alertID
     * @param alertSettings the AlertSettings object that needs to be saved.
     * @return true if saved successfully.
     */
    public boolean saveAlertSettings(AlertSettings alertSettings) {
        Log.i(TAG, "saveAlertSettings()");
        if(updateMode){
            //Update object:
            alertSettings.setId(bundle.getInt("alertID"));
            if(dbService.updateAlertSettings(alertSettings))scheduleAlarm(alertSettings.getId(), alertSettings.getCheckInterval());
            return true;
        }else {
            //Save new object:
            long ok = dbService.addAlertSettings(alertSettings);
            if((int) ok != -1){
                scheduleAlarm((int) ok, alertSettings.getCheckInterval());
                return true;
            }
        }
        return false;
    }

    /**
     * Makes an object form the DefaultSharedPreferences and the custom SharedPreferences (for temperature, precipitation, and wind speed)
     * @param defaultSharedPrefs the default
     * @param sharedPrefs the custom
     * @return an AlertSettings Object
     */
    public AlertSettings makeObjFromPreferences(SharedPreferences defaultSharedPrefs, SharedPreferences sharedPrefs) {
        AlertSettings asd = new AlertSettings();
        asd.setLocation(locationSelected);
        haveSelectedSomething = false;
        Log.i(TAG, "------------ Settings for alert ------------");
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
        Set<String> weekdaysSet = defaultSharedPrefs.getStringSet(getString(R.string.weekdays_pref_key), null);
        if(weekdaysSet == null || weekdaysSet.size() == 0){
            asd.setMon(true); asd.setTue(true); asd.setWed(true); asd.setThu(true); asd.setFri(true); asd.setSat(true); asd.setSun(true);
        }else {
            for(String weekday : weekdaysSet){
                if(weekday.equals("0")) asd.setMon(true);
                if(weekday.equals("1")) asd.setTue(true);
                if(weekday.equals("2")) asd.setWed(true);
                if(weekday.equals("3")) asd.setThu(true);
                if(weekday.equals("4")) asd.setFri(true);
                if(weekday.equals("5")) asd.setSat(true);
                if(weekday.equals("6")) asd.setSun(true);
            }
            Log.i(TAG, "Weekdays: " + weekdaysSet.toString());
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
        Log.i(TAG, "--------------------------------------------");
        return asd;
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     *
     * @param alertId
     * @param interval
     */
    public void scheduleAlarm(int alertId, double interval){
        long intervalLong = (long)interval*3600000; //  3600000 = millisekund i timen
        long startTime = 5000;
        long nowTime = new GregorianCalendar().getTimeInMillis() + startTime;
        Log.e(TAG, "scheduleAlarm: " + alertId);
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        intentAlarm.putExtra("id", alertId);
        intentAlarm.putExtra("url", locationSelected.getXmlURL() );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alertId, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nowTime, intervalLong, pendingIntent);

        //Toast.makeText(this, "Alarm scheduled for Id: " + id + "!", Toast.LENGTH_LONG).show();
    }

    /**
     * For the AlertSettingsPrefFragment.java, if updatemode is true then the previous
     * activity was AlertOverViewActivity and the user whish to update an already existing
     * AlertSettings object in the database. Therefore it needs to initiate the value with the old
     * object values
     * @return boolean
     */
    public boolean getUpdateMode(){
        return updateMode;
    }

    /**
     * If the app gets destroyed or the backbutton gets pressed then the Preferences for the AlertSettings-screen
     * needs to be cleared.
     */
    @Override
    protected void onDestroy() {
        clearPreferencesSaved(this, getString(R.string.name_of_prefs_saved));
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        clearPreferencesSaved(this, getString(R.string.name_of_prefs_saved));
        super.onBackPressed();
    }
}




