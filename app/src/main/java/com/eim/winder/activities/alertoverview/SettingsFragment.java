package com.eim.winder.activities.alertoverview;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eim.winder.R;
import com.eim.winder.databinding.FragmentSettingsBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        AlertOverViewActivity activity = (AlertOverViewActivity) getActivity();
        binding.setAlertsettings(activity.getAlertSettingsDAO());
        binding.setLocation(activity.getAlertSettingsDAO().getLocation());
        TextView weekdays = (TextView) binding.getRoot().findViewById(R.id.row8_column2);
        weekdays.setText(activity.getAlertSettingsDAO().getWeekDaysSelected(getResources()));
        return binding.getRoot();
    }

}
