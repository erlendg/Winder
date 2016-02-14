package com.eim.winder;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.eim.winder.db.LocationDAO;

import java.lang.reflect.Constructor;

/**
 * Created by Mari on 09.02.2016.
 */
public class CustomTextChangedListener implements TextWatcher{
    public static final String TAG = "CustomTCListener";
    Context context;

    public CustomTextChangedListener(Context context) {
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence input, int start, int before, int count) {
        Log.e(TAG, "User input: " + input);
        AlertSettingsActivity alertsetActivity = ((AlertSettingsActivity) context);
        //send query to database based on user input:
        alertsetActivity.searchLocations = alertsetActivity.datasource.readSearch(input.toString());

        // update the adapater
        alertsetActivity.searchAdapter.notifyDataSetChanged();
        alertsetActivity.searchAdapter = new ArrayAdapter<LocationDAO>(alertsetActivity, android.R.layout.simple_dropdown_item_1line, alertsetActivity.searchLocations);
        alertsetActivity.searchView.setAdapter(alertsetActivity.searchAdapter);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
