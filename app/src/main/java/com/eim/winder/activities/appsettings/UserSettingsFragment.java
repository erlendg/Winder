package com.eim.winder.activities.appsettings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.eim.winder.R;

/**
 * Created by Erlend on 27.04.2016.
 */
public class UserSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

}
