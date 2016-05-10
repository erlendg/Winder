package com.eim.winder.activities.alertsettings;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.eim.winder.R;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by Mari on 05.04.2016.
 */
public class AlertSettingsPrefFragment extends PreferenceFragment {
    private static final String TAG = "AlertSettingsPrefFragment";
    Preference.OnPreferenceChangeListener changeListener;
    CheckBoxPreference tempPref;
    CheckBoxPreference precipPref;
    CheckBoxPreference windSpeedPref;
    CheckBoxPreference windDirPref;
    CheckBoxPreference sunnyPref;
    ListPreference checkIntrPref;
    Preference tempRange;
    Preference precipRange;
    Preference windSpeedRange;
    MultiSelectListPreference windDir;
    MultiSelectListPreference weekdays;
    SharedPreferences prefs;
    SharedPreferences defaultPrefs;
    AlertSettingsActivityBeta activity;

    /**
     * Creates the inflated AlertSettingsPrefFragment view alert_preferences.xml inside the view of AlertSettingsActivityBeta.
     * Builds all preferences and the onPreferencesChanged listener.
     * Fetches the current activity.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.alert_preferences);
        //The current activity:
        activity = (AlertSettingsActivityBeta) getActivity();
        PreferenceManager.setDefaultValues(activity.getApplicationContext(), R.xml.alert_preferences, true);
        buildPrefViewComponents();
        initiateTemplatePrefs();
    }

    /**
     * Finds and builds view components
     */
    private void buildPrefViewComponents(){
        //Visible CheckBoxPreferences and ListPreferences:
        tempPref = (CheckBoxPreference) findPreference(getResources().getString(R.string.temp_pref_key));
        precipPref = (CheckBoxPreference) findPreference(getResources().getString(R.string.precip_pref_key));
        windSpeedPref= (CheckBoxPreference)findPreference(getResources().getString(R.string.windspeed_pref_key));
        windDirPref = (CheckBoxPreference)findPreference(getResources().getString(R.string.winddir_pref_key));
        sunnyPref = (CheckBoxPreference)findPreference(getResources().getString(R.string.sunny_pref_key));
        checkIntrPref = (ListPreference)findPreference(getResources().getString(R.string.checkintr_pref_key));
        //The Rangebar preferences and the MultiSelectPreferences:
        tempRange = findPreference(getResources().getString(R.string.temp_range_key));
        precipRange = findPreference(getResources().getString(R.string.precip_range_key));
        windSpeedRange = findPreference(getResources().getString(R.string.windspeed_range_key));
        windDir = (MultiSelectListPreference) findPreference(getResources().getString(R.string.winddir_select_key));
        weekdays = (MultiSelectListPreference) findPreference(getResources().getString(R.string.weekdays_pref_key));
        //Creates the change listener:
        setPrefsChangedListener();
        //Sets the change listener to the preferences:
        tempPref.setOnPreferenceChangeListener(changeListener);
        precipPref.setOnPreferenceChangeListener(changeListener);
        windSpeedPref.setOnPreferenceChangeListener(changeListener);
        checkIntrPref.setOnPreferenceChangeListener(changeListener);
        windDirPref.setOnPreferenceChangeListener(changeListener);
        windDir.setOnPreferenceChangeListener(changeListener);
        weekdays.setOnPreferenceChangeListener(changeListener);
        //remove the non-selected preferences from screen:
        getPreferenceScreen().removePreference(tempRange);
        getPreferenceScreen().removePreference(precipRange);
        getPreferenceScreen().removePreference(windSpeedRange);
        getPreferenceScreen().removePreference(windDir);
    }
    /**
     * Initializes the changeLister with OnPreferenceChangeListener
     */
    public void setPrefsChangedListener(){
        changeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return prefsChanged(preference, newValue);
            }
        };
    }

    /**
     * The method sorts the actions based on which preference that was changed
     * @param pref the changed preference
     * @param newValue the new value from the preference
     * @return true if a change has occurred
     */
    public boolean prefsChanged(Preference pref, Object newValue){
        prefs = activity.getApplicationContext().getSharedPreferences(getString(R.string.name_of_prefs_saved), activity.getApplicationContext().MODE_PRIVATE);
       //Show or hide preference if boolean:
        if(newValue instanceof Boolean) {
            boolean checked = (Boolean) newValue;
            return showOrHidePref(checked, pref);
        }
        //if check interval preference then set new summary description and save the value to SharedPreference:
        if(pref == checkIntrPref){
            CharSequence[] entries = checkIntrPref.getEntries();
            int id = Integer.parseInt((String)newValue);
            checkIntrPref.setSummary(entries[(int) id]);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getResources().getString(R.string.checkintr_pref_key), (entries[(int) id]).toString());
            editor.apply();
            return true;
            //MultiSelectPreference needs a more complicated handling:
        }if(pref == windDir){
            SharedPreferences.Editor editor = prefs.edit();
            setMultiSelectWinDirPrefSummary(windDir, newValue, getResources().getString(R.string.winddir_select_key), getResources().getString(R.string.choose_winddirection_string), editor);
            return true;
            //MultiSelectPreference needs a more complicated handling:
        }if(pref == weekdays){
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
            setMultiSelectWeekdaysPrefSummary(weekdays, newValue, getResources().getString(R.string.weekdays_pref_key), getResources().getString(R.string.choose_weekdays_string), editor);
            return true;
        }
        return false;
    }

    /**
     * Since the preference summary needs to be set and should show the selected weekdays for the user
     * we have to sort the array (newValue) of selection so the weekdays are shown in the correct order. Monday-Saturday
     * @param multipref the preference which has been changes
     * @param newValue the new value of the preference
     * @param saveName the name of the SharedPreferences to save to
     * @param defValue the default value of the preference value
     * @param editor the editor who stores the values inside SharedPreferences.
     */
    private void setMultiSelectWeekdaysPrefSummary(MultiSelectListPreference multipref, Object newValue, String saveName, String defValue, SharedPreferences.Editor editor){
        String summary = "";
        CharSequence[] entries = multipref.getEntries();
        Set<String> selections = (Set<String>) newValue;
        String[] selected = selections.toArray(new String[selections.size()]);
        //Set default summary if nothing is selected
        if(selected.length == 0 ) {
            multipref.setSummary(defValue);
            editor.putStringSet(saveName, null);
            //if something is selected: sort the values, update the summary and store it inside SharedPreferences
        }else{
            int[] help = new int[selected.length];
            for(int i = 0; i < selected.length; i++){
                help[i] = Integer.parseInt(selected[i]);
            }
            Arrays.sort(help);
            for (int i = 0; i < help.length; i++) {
                int id = help[i];
                if (i == help.length - 1) {
                    summary += entries[id];
                } else {
                    summary += entries[id] + ", ";
                }
            }
            editor.putStringSet(saveName, selections);
            multipref.setSummary(summary);
        }
        editor.apply();
    }

    /**
     * Same as the setMultiselectedWindDirPrefSummary, but here there needs to be stored a string and not an stringSet.
     * @param multipref the preference which has been changes
     * @param newValue the new value of the preference
     * @param saveName the name of the SharedPreferences to save to
     * @param defValue the default value of the preference value
     * @param editor the editor who stores the values inside SharedPreferences.
     */
    private void setMultiSelectWinDirPrefSummary(MultiSelectListPreference multipref, Object newValue, String saveName, String defValue, SharedPreferences.Editor editor){
        String res = "";
        CharSequence[] entries = multipref.getEntries();
        Set<String> selections = (Set<String>) newValue;
        String[] selected = selections.toArray(new String[selections.size()]);
        int[] help = new int[selected.length];
        //Set default summary if nothing is selected
        if(selected.length == 0 ){
            multipref.setSummary(defValue);
            editor.putString(saveName, "NOT VALID");
        }else {
            for(int i = 0; i < selected.length; i++){
                help[i] = Integer.parseInt(selected[i]);
            }
            Arrays.sort(help);
            for (int i = 0; i < help.length; i++) {
                int id = help[i];
                if (i == help.length - 1) {
                    res += entries[id];
                } else {
                    res += entries[id] + ", ";
                }
            }
            editor.putString(saveName, res);
            multipref.setSummary(res);
        }
        editor.apply();
    }

    /**
     * Shows or hides Preference on screen based on selected/unselected value
     * @param checked if the preference is checked or not
     * @param pref the current preference
     * @return true if something is changed.
     */
    public boolean showOrHidePref(boolean checked, Preference pref){
        if (checked) {
            if (pref == tempPref ) {
                getPreferenceScreen().addPreference(tempRange);
                return true;
            }
            if (pref == precipPref) {
                getPreferenceScreen().addPreference(precipRange);
                return true;
            }
            if (pref == windSpeedPref) {
                getPreferenceScreen().addPreference(windSpeedRange);
                return true;
            }
            if (pref == windDirPref){
                getPreferenceScreen().addPreference(windDir);
                return true;
            }
        } if(!checked){
            if (pref == tempPref) {
                getPreferenceScreen().removePreference(tempRange);
                return true;
            }
            if (pref == precipPref) {
                getPreferenceScreen().removePreference(precipRange);
                return true;
            }
            if (pref == windSpeedPref) {
                getPreferenceScreen().removePreference(windSpeedRange);
                return true;
            }
            if(pref == windDirPref){
                getPreferenceScreen().removePreference(windDir);
                return true;
            }
        }
        return false;
    }

    /**
     * Builds the PreferenceScreen based on selected weather template form SelectLocationActivity:
     */
    public void initiateTemplatePrefs(){
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(tempPref.isChecked()) {
            getPreferenceScreen().addPreference(tempRange);
        }if(precipPref.isChecked()) {
            getPreferenceScreen().addPreference(precipRange);
        }if(windSpeedPref.isChecked()) {
            getPreferenceScreen().addPreference(windSpeedRange);
        }
        // If the previous activity was AlertSettingsOverview then there are already settings to be shown and defaults must be overridden.
        if(activity.getUpdateMode()) {
            if(windDirPref.isChecked()) {
                getPreferenceScreen().addPreference(windDir);
                prefsChanged(windDir, defaultPrefs.getStringSet(getString(R.string.winddir_select_key), null));
            }
            prefsChanged(weekdays, defaultPrefs.getStringSet(getString(R.string.weekdays_pref_key), null));
            prefsChanged(checkIntrPref, defaultPrefs.getString(getString(R.string.checkintr_pref_key), null));
        }
    }
}
