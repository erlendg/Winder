package com.eim.winder.activities.alertoverview;


import android.content.Context;
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
import com.eim.winder.db.AlertSettingsDAO;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private static String TAG = "SettingsFragment";
    private LinearLayout fromToLayout;
    private LinearLayout tempLayout;
    private LinearLayout precipLayout;
    private LinearLayout windLayout;
    private LinearLayout windDirLayout;
    private LinearLayout sunLayout;
    private TextView sunCheck;
    private AlertOverViewActivity activity;
    private AlertSettingsDAO alertSettingsDAO;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        activity = (AlertOverViewActivity) getActivity();
        alertSettingsDAO = activity.getAlertSettingsDAO();
        binding.setAlertsettings(alertSettingsDAO);
        binding.setLocation(alertSettingsDAO.getLocation());
        View v = binding.getRoot();
        TextView weekdays = (TextView) v.findViewById(R.id.row8_column2);
        weekdays.setText(alertSettingsDAO.getWeekDaysSelected(getResources()));
        removeDefaultValues(v, activity.getAlertSettingsDAO());
        return v;
    }

    /**
     * Removes defaultvalues from settingsview, so only set preferences is shown
     * @param view
     * @param asd
     */
    public void removeDefaultValues(View view, AlertSettingsDAO asd){
        //Log.d(TAG, "prøver å oppdatere...");
        fromToLayout = (LinearLayout) view.findViewById(R.id.row2_fromTo);
        tempLayout = (LinearLayout) view.findViewById(R.id.row3_temp);
        precipLayout = (LinearLayout) view.findViewById(R.id.row4_precip);
        windLayout = (LinearLayout) view.findViewById(R.id.row5_wind);
        windDirLayout = (LinearLayout)view.findViewById(R.id.row6_windDir);
        sunLayout = (LinearLayout)view.findViewById(R.id.row7_sun);
        sunCheck = (TextView) view.findViewById(R.id.row7_column2);
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

}
