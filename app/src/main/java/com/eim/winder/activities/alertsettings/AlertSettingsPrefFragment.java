package com.eim.winder.activities.alertsettings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eim.winder.R;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
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


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.alert_preferences);


        tempPref = (CheckBoxPreference) findPreference(getResources().getString(R.string.temp_pref_key));
        precipPref = (CheckBoxPreference) findPreference(getResources().getString(R.string.precip_pref_key));
        windSpeedPref= (CheckBoxPreference)findPreference(getResources().getString(R.string.windspeed_pref_key));
        windDirPref = (CheckBoxPreference)findPreference(getResources().getString(R.string.winddir_pref_key));
        sunnyPref = (CheckBoxPreference)findPreference(getResources().getString(R.string.sunny_pref_key));
        checkIntrPref = (ListPreference)findPreference(getResources().getString(R.string.checkintr_pref_key));

        tempPref = (CheckBoxPreference) findPreference("tempPref");
        precipPref = (CheckBoxPreference) findPreference("precipPref");
        windSpeedPref= (CheckBoxPreference)findPreference("windSpeedPref");
        windDirPref = (CheckBoxPreference)findPreference("windDirPref");
        sunnyPref = (CheckBoxPreference)findPreference("sunnyPref");
        checkIntrPref = (ListPreference)findPreference("checkIntrPref");


        tempRange = findPreference(getResources().getString(R.string.temp_range_key));
        precipRange = findPreference(getResources().getString(R.string.precip_range_key));
        windSpeedRange = findPreference(getResources().getString(R.string.windspeed_range_key));
        windDir = (MultiSelectListPreference) findPreference(getResources().getString(R.string.winddir_select_key));
        weekdays = (MultiSelectListPreference) findPreference(getResources().getString(R.string.weekdays_pref_key));
        //weekdays.setValues(new HashSet<String>(Arrays.asList(getResources().getStringArray(R.array.weekdays_values))));

        setPrefsChangedListener();

        tempPref.setOnPreferenceChangeListener(changeListener);
        precipPref.setOnPreferenceChangeListener(changeListener);
        windSpeedPref.setOnPreferenceChangeListener(changeListener);
        checkIntrPref.setOnPreferenceChangeListener(changeListener);
        windDirPref.setOnPreferenceChangeListener(changeListener);
        windDir.setOnPreferenceChangeListener(changeListener);
        weekdays.setOnPreferenceChangeListener(changeListener);

        getPreferenceScreen().removePreference(tempRange);
        getPreferenceScreen().removePreference(precipRange);
        getPreferenceScreen().removePreference(windSpeedRange);
        getPreferenceScreen().removePreference(windDir);

        initializeTemplatePrefs();

    }

    public void setPrefsChangedListener(){
        changeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return prefsChanged(preference, newValue);
            }
        };
    }
    public boolean prefsChanged(Preference pref, Object newValue){
        prefs = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.name_of_prefs_saved), getActivity().getApplicationContext().MODE_PRIVATE);
        if(newValue instanceof Boolean) {
            boolean checked = (Boolean) newValue;
            return showOrHidePref(checked, pref);
        }
        if(pref == checkIntrPref){
            CharSequence[] entries = checkIntrPref.getEntries();
            int id = Integer.parseInt((String)newValue);
            checkIntrPref.setSummary(entries[(int) id]);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getResources().getString(R.string.checkintr_pref_key), (entries[(int) id]).toString());
            editor.apply();
            return true;
        }if(pref == windDir){
            SharedPreferences.Editor editor = prefs.edit();
            setMultiSelectPreferenceSummary(windDir, newValue, getResources().getString(R.string.winddir_select_key), getResources().getString(R.string.choose_winddirection_string) , editor, false);
            return true;

        }if(pref == weekdays){
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
            setMultiSelectPreferenceSummary(weekdays, newValue, getResources().getString(R.string.weekdays_pref_key), getResources().getString(R.string.choose_weekdays_string), editor, true);
            return true;
        }
        return false;
    }
    private void setMultiSelectPreferenceSummary(MultiSelectListPreference multipref, Object newValue, String saveName, String defValue, SharedPreferences.Editor editor, boolean saveIntSet){
        String res = "";
        //SharedPreferences.Editor editor;
        if(newValue == null){
            multipref.setSummary(defValue);
            return;
        }
        CharSequence[] entries = multipref.getEntries();
        Set<String> selections = (Set<String>) newValue;
        String[] selected = selections.toArray(new String[selections.size()]);
        int[] help = new int[selected.length];
        if(selected.length == 0 ){
            multipref.setSummary(defValue);
            editor.putString(saveName, "NOT VALID");
            //Log.i("HEIEHI","lenght = 0" );
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
            Log.i("HEIEHI", "" + selections.toString() + " " + res);
            if(saveIntSet){
                editor.putStringSet(saveName, selections);
            }
            else {
                editor.putString(saveName, res);
            }
            multipref.setSummary(res);
        }
        editor.apply();
    }
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
    public void initializeTemplatePrefs(){
        if(tempPref.isChecked()) {
            getPreferenceScreen().addPreference(tempRange);
        }if(precipPref.isChecked()) {
            getPreferenceScreen().addPreference(precipRange);
        }if(windSpeedPref.isChecked()){
            getPreferenceScreen().addPreference(windSpeedRange);
        }if(windDirPref.isChecked()) {
            getPreferenceScreen().addPreference(windDir);
            SharedPreferences defSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            prefsChanged(windDir, defSharedPrefs.getStringSet(getString(R.string.winddir_select_key), null));
        }
    }
}
