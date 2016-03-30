package com.eim.winder.activities;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eim.winder.R;
import com.eim.winder.activities.AlertOverViewActivity;
import com.eim.winder.databinding.ContentAlertOverViewBinding;


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
        ContentAlertOverViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.content_alert_over_view, container, false);
        AlertOverViewActivity activity = (AlertOverViewActivity) getActivity();
        binding.setAlertsettings(activity.getAlertSettingsDAO());
        binding.setLocation(activity.getAlertSettingsDAO().getLocation());
        return binding.getRoot();
    }

}
