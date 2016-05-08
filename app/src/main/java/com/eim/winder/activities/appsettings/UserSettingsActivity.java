package com.eim.winder.activities.appsettings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eim.winder.R;

/**
 * Created by Erlend on 28.04.2016.
 */
public class UserSettingsActivity extends AppCompatActivity {
    /**
     * Creates the view for the app setting (UserSettingActivity): appsettings_layout.xml
     * builds the toolbar, icon and name,
     * inflates the UserSettingsFragment with the PreferenceScreen and
     * sets its default values
     * @param savedInstanceState bundle of the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appsettings_layout);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_stat_name);
        getFragmentManager().beginTransaction().replace(R.id.appSettingsFrame, new UserSettingsFragment()).commit();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }
}
