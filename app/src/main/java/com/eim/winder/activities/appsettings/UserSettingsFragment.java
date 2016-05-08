package com.eim.winder.activities.appsettings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eim.winder.R;
import com.eim.winder.activities.main.MainActivity;

import java.util.Locale;

/**
 * Created by Mari on 29.04.2016.
 */
public class UserSettingsFragment extends PreferenceFragment {
    private ListPreference languagePref;
    private Preference.OnPreferenceChangeListener changeListener;
    private SharedPreferences prefs;

    /**
     * Creates the inflated view inside UserSettingsActivity
     * and initiates view components.
     * @param savedInstanceState
     */

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiateViewComponents();
    }

    /**
     * Adds xml-layout file, finds and initiates preference references and sets an onPreferenceChangedListener
     */
    private void initiateViewComponents(){
        addPreferencesFromResource(R.xml.settings);
        languagePref = (ListPreference) findPreference(getString(R.string.language_pref_key));
        setPrefsChangedListener();
        languagePref.setOnPreferenceChangeListener(changeListener);
    }

    /**
     * Sets the onChangedListener
     */
    public void setPrefsChangedListener() {
        changeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return prefsChanged(preference, newValue);
            }
        };
    }

    /**
     * Stores values inside sharedPreferences before the default SharedPreferences has noticed changes,
     * stores the new values and loads the view again so the language selection is visually present to the user.
     * @param pref preference that was changed
     * @param newValue the new value
     * @return boolean, true if the preference was changed
     */
    public boolean prefsChanged(Preference pref, Object newValue) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Locale l;
        if (pref == languagePref) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.language_pref_key), newValue.toString());
            editor.apply();
            String selectedLanguage = prefs.getString(getString(R.string.language_pref_key), "default");
            l = getResources().getConfiguration().locale;
            Log.i("NewValue", newValue.toString() + ", " + selectedLanguage);
            MainActivity.setApplicationLocale(l, getActivity().getApplicationContext());
            setPreferenceScreen(null);
            initiateViewComponents();
            return true;
        }
        return false;
    }
}
