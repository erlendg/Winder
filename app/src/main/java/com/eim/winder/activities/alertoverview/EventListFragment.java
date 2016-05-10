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
import com.eim.winder.db.DBService;
import com.eim.winder.db.Forecast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {
    private RecyclerView eventList;
    private ArrayList<Forecast> forecastList;
    private EventListRVAdapter listAdapter;
    private DBService dbService;
    private AlertSettings alertSettings;
    private LinearLayoutManager llmanager;

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Inflates the weather event list inside AlertOverViewActivity
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view with the forecast list
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dbService = new DBService(getContext());
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        alertSettings = ((AlertOverViewActivity) getActivity()).getAlertSettings();
        forecastList = dbService.getForecastsById(alertSettings.getId());
        eventList = (RecyclerView) view.findViewById(R.id.event_listview);
        //Populates the weather/forecast event list:
        if(forecastList != null) {
            listAdapter = new EventListRVAdapter(forecastList);
            llmanager = new LinearLayoutManager(getContext());
            eventList.setLayoutManager(llmanager);
            eventList.setAdapter(listAdapter);
            //If there is no forecasts (or weather events found) the display a message:
            if (forecastList.isEmpty()) {
                TextView emptyListText = (TextView) view.findViewById(R.id.empty_event_list);
                emptyListText.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

}
