package com.eim.winder.activities.appsettings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.eim.winder.R;

import java.util.List;

/**
 * Created by Erlend on 28.04.2016.
 */
public class UserSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //android.R.id.content
        //or
        //R.id.settingsFragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new UserSettingsFragment()).commit();
    }

}
