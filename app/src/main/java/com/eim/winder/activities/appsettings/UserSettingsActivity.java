package com.eim.winder.activities.appsettings;

import android.os.Bundle;
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
        getFragmentManager().beginTransaction().replace(R.id.appSettingsFrame, new UserSettingsFragment()).commit();
    }
}
