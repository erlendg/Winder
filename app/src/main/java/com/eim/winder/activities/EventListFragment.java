package com.eim.winder.activities;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.ForecastDAO;
import com.eim.winder.db.ForecastDSService;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {
    private ListView eventList;
    private ArrayList<ForecastDAO> forecastList;
    private ArrayAdapter listAdapter;
    private ForecastDSService datasource;
    private AlertSettingsDAO alertSettingsDAO;


    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        datasource = new ForecastDSService(getContext());
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);
        alertSettingsDAO = ((AlertOverViewActivity) getActivity()).getAlertSettingsDAO();
        forecastList = datasource.getAllForecastsByAlertSettingsID(alertSettingsDAO.getId());
        eventList = (ListView) getActivity().findViewById(R.id.event_listview);

        return v;
    }

}
