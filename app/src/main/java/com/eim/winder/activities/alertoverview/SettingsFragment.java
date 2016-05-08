package com.eim.winder.activities.alertoverview;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eim.winder.R;
import com.eim.winder.databinding.FragmentSettingsBinding;
import com.eim.winder.db.AlertSettings;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private static String TAG = "SettingsFragment";


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Inflates the weather settings for the selected AlertSettings and removes default unused values.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        AlertOverViewActivity activity = (AlertOverViewActivity) getActivity();
        AlertSettings alertSettings = activity.getAlertSettings();
        binding.setAlertsettings(alertSettings);
        binding.setLocation(alertSettings.getLocation());
        View v = binding.getRoot();
        TextView weekdays = (TextView) v.findViewById(R.id.row8_column2);
        weekdays.setText(alertSettings.getWeekDaysSelected(getResources()));
        removeDefaultValues(v, activity.getAlertSettings());
        setCheckInterval(alertSettings.getCheckInterval(), v);
        return v;
    }

    /**
     * Removes default values from the settings view, so only set preferences is shown
     * @param view that displayed the settings table fragment_settings.xml
     * @param asd the AlertSettings object
     */
    public void removeDefaultValues(View view, AlertSettings asd){
        //Log.d(TAG, "prøver å oppdatere...");
        LinearLayout fromToLayout = (LinearLayout) view.findViewById(R.id.row2_fromTo);
        LinearLayout tempLayout = (LinearLayout) view.findViewById(R.id.row3_temp);
        LinearLayout precipLayout = (LinearLayout) view.findViewById(R.id.row4_precip);
        LinearLayout windLayout = (LinearLayout) view.findViewById(R.id.row5_wind);
        LinearLayout windDirLayout = (LinearLayout) view.findViewById(R.id.row6_windDir);
        LinearLayout sunLayout = (LinearLayout) view.findViewById(R.id.row7_sun);
        TextView sunCheck = (TextView) view.findViewById(R.id.row7_column2);
        if(asd.getTempMin() == asd.DEFAULT_TEMP && asd.getPrecipitationMin() == asd.DEFAULT_WIND_AND_PRECIP && asd.getWindSpeedMin() == asd.DEFAULT_WIND_AND_PRECIP ){
            fromToLayout.setVisibility(View.GONE);
            tempLayout.setVisibility(View.GONE);
            precipLayout.setVisibility(View.GONE);
            windLayout.setVisibility(View.GONE);
        }else {
            if (asd.getTempMin() == asd.DEFAULT_TEMP) { // Only need to check one of them, max and min are then equal
                tempLayout.setVisibility(View.GONE);
               // Log.d(TAG, "prøver å oppdatere temp");
            }
            if (asd.getPrecipitationMin() == asd.DEFAULT_WIND_AND_PRECIP) {
                precipLayout.setVisibility(View.GONE);
                //Log.d(TAG, "prøver å oppdatere nedbør");
            }
            if (asd.getWindSpeedMin() == asd.DEFAULT_WIND_AND_PRECIP) {
                windLayout.setVisibility(View.GONE);
                //Log.d(TAG, "prøver å oppdatere vind");
            }
        }
        if (asd.getWindDirection() == null) {
            windDirLayout.setVisibility(View.GONE);
        }
        if (!asd.isCheckSun()) {
            sunLayout.setVisibility(View.GONE);
        }else{
            sunCheck.setText(getString(R.string.yes));
        }
    }

    /**
     * Manually sets the field "check interval" from AlertSettings so the user sees string value and not a default
     * double value.
     * @param checkIntr number of the check interval
     * @param v the view of the components that needs to be updated
     */
    private void setCheckInterval(double checkIntr, View v){
        TextView checkText = (TextView) v.findViewById(R.id.row9_column2);
        String[] intrStringArray = getResources().getStringArray(R.array.checkinterval_array);
        if(checkIntr == 1.0){
            checkText.setText(intrStringArray[0]);
            return;
        }if(checkIntr == 2.0){
            checkText.setText(intrStringArray[1]);
            return;
        }if(checkIntr == 4.0){
            checkText.setText(intrStringArray[2]);
            return;
        }if(checkIntr == 6.0){
            checkText.setText(intrStringArray[3]);
            return;
        }if(checkIntr == 12.0){
            checkText.setText(intrStringArray[4]);
            return;
        }if(checkIntr == 24.0){
            checkText.setText(intrStringArray[5]);
            return;
        }else {
            checkText.setText(intrStringArray[6]);
        }

    }

}
