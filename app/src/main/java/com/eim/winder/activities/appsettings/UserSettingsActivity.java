package com.eim.winder.activities.appsettings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import com.eim.winder.R;

/**
 * Created by Erlend on 28.04.2016.
 */
public class UserSettingsActivity extends AppCompatActivity {

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
