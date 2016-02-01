package com.eim.winder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.text.TextUtils;

/**
 * Created by Mari on 31.01.2016.
 */
public class AlertSettingsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "SearchViewFilterMode";

    private SearchView mSearchView;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    private final String[] mStrings = Locations.locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.alertsettings_layout);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mStrings));
        mListView.setTextFilterEnabled(true);
        setupSearchView();
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
       // mSearchView.setQueryHint(getString(R.string.cheese_hunt_hint));
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}






