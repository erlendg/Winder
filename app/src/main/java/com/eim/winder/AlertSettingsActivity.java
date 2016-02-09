package com.eim.winder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.text.TextUtils;

import com.eim.winder.db.LocationDAO;
import com.eim.winder.db.LocationDataSourceService;
import com.eim.winder.CustomAutoCompleteView;

import java.util.List;

/**
 * Created by Mari on 31.01.2016.
 */
public class AlertSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AlertSettingsActivity";

    private LocationDataSourceService datasource;
    public CustomAutoCompleteView searchView;
    public ListView mListView;
    public ArrayAdapter<String> searchAdapter;

    public String[] searchLocations = {" "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertsettings_layout);
        // instantiate database handler
        datasource = new LocationDataSourceService(this);
        // autocompletetextview is in alertsettings_layout.xml
        searchView = (CustomAutoCompleteView) findViewById(R.id.search_view);
        searchView.addTextChangedListener(new CustomTextChangedListener(this));
        searchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchLocations);
        searchView.setAdapter(searchAdapter);

    }
    // this function is used in CustomAutoCompleteTextChangedListener.java
    public String[] getSearchedItemsFromDb(String searchTerm) {
        Log.i(TAG, "getSearchedItemsFromDb()");
        // add items on the array:
        List<LocationDAO> searchRes = datasource.readSearch(searchTerm);
        String[] res = new String[searchRes.size()];
        for (int i = 0; i < searchRes.size(); i++) {
            res[i] = searchRes.get(i).toString();
        }
        return res;
    }

    @Override
    protected void onResume() {
        //datasource.open();
        datasource = new LocationDataSourceService(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }


}






