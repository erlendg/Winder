package com.eim.winder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.text.TextUtils;

/**
 * Created by Mari on 31.01.2016.
 */
public class AlertSettingsActivity extends AppCompatActivity {
    private static final String TAG = "SearchViewFilterMode";

    private AutoCompleteTextView searchView;
    private ListView mListView;
    private ArrayAdapter<String> searchAdapter;

    private final String[] locations = Locations.locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertsettings_layout);

        searchView = (AutoCompleteTextView) findViewById(R.id.search_view);
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
        //Set the number of characters the user must type before the drop down list is shown
        searchView.setThreshold(1);
        searchView.setAdapter(searchAdapter);
    }

}






