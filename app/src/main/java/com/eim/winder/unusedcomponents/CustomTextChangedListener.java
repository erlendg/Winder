package com.eim.winder.unusedcomponents;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.eim.winder.activities.alertsettings.AlertSettingsActivity;

/**
 * Created by Mari on 09.02.2016.
 * Custom Text changed listener for the autocomplete textview that runs an sql-search query for every
 * input string form the user. This is to avoid loading the entire table content into the autocomplete adapter when
 * the location list maybe contains over 100 000 locations. For now, to load 11 000 locations is not a problem for
 * the adapter and is therefore loaded at once, when the user starts searching for locations.
 */
public class CustomTextChangedListener implements TextWatcher{
    final private AlertSettingsActivity alertsetActivity;
    public static final String TAG = "CustomTCListener";
    Context context;

    public CustomTextChangedListener(Context context) {
        this.context = context;
        alertsetActivity = ((AlertSettingsActivity) context);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence input, int start, int before, int count) {
        if(input != null || !input.toString().equals(" ")) {
            Log.e(TAG, "User input: " + input);

            //send query to database based on user input:
            //alertsetActivity.searchLocations = alertsetActivity.datasource.readSearch(input.toString());
            /*alertsetActivity.searchLocations2 = alertsetActivity.datasource.readSearch(input.toString());
            Log.e(TAG, "Array size: " + alertsetActivity.searchLocations.size());
            // update the adapater

            alertsetActivity.searchAdapter = new ArrayAdapter<Location>(alertsetActivity, android.R.layout.simple_dropdown_item_1line, alertsetActivity.searchLocations);
            Log.e(TAG, "Array size: " + alertsetActivity.searchLocations.size());
            alertsetActivity.searchView.setAdapter(alertsetActivity.searchAdapter);
            Log.e(TAG, "Array size: " + alertsetActivity.searchLocations.size());
            alertsetActivity.searchAdapter.notifyDataSetChanged();
            Log.e(TAG, "Array size: " + alertsetActivity.searchLocations.size());*/
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
