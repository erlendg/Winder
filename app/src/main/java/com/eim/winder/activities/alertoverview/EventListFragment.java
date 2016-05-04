package com.eim.winder.activities.alertoverview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.Forecast;
import com.eim.winder.db.ForecastRepo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {
    private RecyclerView eventList;
    private ArrayList<Forecast> forecastList;
    private EventListRVAdapter listAdapter;
    private ForecastRepo datasource;
    private AlertSettings alertSettings;
    private LinearLayoutManager llmanager;

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        datasource = new ForecastRepo(getContext());
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);
        alertSettings = ((AlertOverViewActivity) getActivity()).getAlertSettings();
        forecastList = datasource.getAllForecastsByAlertSettingsID(alertSettings.getId());
        eventList = (RecyclerView) v.findViewById(R.id.event_listview);
        if(forecastList != null) {
            listAdapter = new EventListRVAdapter(forecastList);
            llmanager = new LinearLayoutManager(getContext());
            eventList.setLayoutManager(llmanager);
            eventList.setAdapter(listAdapter);
            if (forecastList.isEmpty()) {
                TextView emptyListText = (TextView) v.findViewById(R.id.empty_event_list);
                emptyListText.setVisibility(View.VISIBLE);
            }
        }
        return v;
    }

}
