package com.eim.winder.activities.alertsettings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.eim.winder.R;

import java.util.Arrays;
import java.util.HashSet;
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


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.alert_preferences);

        tempPref = (CheckBoxPreference) findPreference("tempPref");
        precipPref = (CheckBoxPreference) findPreference("precipPref");
        windSpeedPref= (CheckBoxPreference)findPreference("windSpeedPref");
        windDirPref = (CheckBoxPreference)findPreference("windDirPref");
        sunnyPref = (CheckBoxPreference)findPreference("sunnyPref");
        checkIntrPref = (ListPreference)findPreference("checkIntrPref");

        tempRange = findPreference("tempRange");
        precipRange = findPreference("precipRange");
        windSpeedRange = findPreference("windSpeedRange");
        windDir = (MultiSelectListPreference) findPreference("windDir");
        weekdays = (MultiSelectListPreference) findPreference("weekdays");
        weekdays.setValues(new HashSet<String>(Arrays.asList(getResources().getStringArray(R.array.weekdays_values))));

        setPrefsChangedListener();

        tempPref.setOnPreferenceChangeListener(changeListener);
        precipPref.setOnPreferenceChangeListener(changeListener);
        windSpeedPref.setOnPreferenceChangeListener(changeListener);
        checkIntrPref.setOnPreferenceChangeListener(changeListener);
        windDirPref.setOnPreferenceChangeListener(changeListener);
        windDir.setOnPreferenceChangeListener(changeListener);

        getPreferenceScreen().removePreference(tempRange);
        getPreferenceScreen().removePreference(precipRange);
        getPreferenceScreen().removePreference(windSpeedRange);
        getPreferenceScreen().removePreference(windDir);

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
        if(newValue instanceof Boolean) {
            boolean checked = (Boolean) newValue;
            return showOrHidePref(checked, pref);
        }
        if(pref == checkIntrPref){
            CharSequence[] entries = checkIntrPref.getEntries();
            int id = Integer.parseInt((String)newValue);
            checkIntrPref.setSummary(entries[(int) id-1]);
            prefs = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.name_of_prefs_saved), getActivity().getApplicationContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("checkIntrPref",(entries[(int) id-1]).toString());
            editor.commit();
            return true;
        }if(pref == windDir){
            String res = "";
            CharSequence[] entries = windDir.getEntries();
            Set<String> selections = (Set<String>) newValue;
            String[] selected = selections.toArray(new String[]{});
            prefs = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.name_of_prefs_saved), getActivity().getApplicationContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if(selected.length == 0 ){
                windDir.setSummary("Choose wind direction");
                editor.putString("windDir", "NOT VALID");
            }else {
                for (int i = 0; i < selected.length; i++) {
                    int id = Integer.parseInt(selected[i].toString());
                    if (i == selected.length - 1) {
                        res += entries[id - 1];
                    } else {
                        res += entries[id - 1] + ", ";
                    }
                }
                editor.putString("windDir", res);
                windDir.setSummary(res);
            }
            editor.commit();
            return true;
        }
        return false;
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
}
