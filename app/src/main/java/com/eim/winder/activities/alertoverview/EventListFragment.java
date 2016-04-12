package com.eim.winder.activities.alertoverview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.ForecastDAO;
import com.eim.winder.db.ForecastDSService;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {
    private RecyclerView eventList;
    private ArrayList<ForecastDAO> forecastList;
    private EventListRVAdapter listAdapter;
    private ForecastDSService datasource;
    private AlertSettingsDAO alertSettingsDAO;
    private LinearLayoutManager llmanager;

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
        eventList = (RecyclerView) v.findViewById(R.id.event_listview);
        if(forecastList != null || forecastList.size() == 0) {
            Log.d("§§§§§§", "ikke null");
            listAdapter = new EventListRVAdapter(forecastList);
            llmanager = new LinearLayoutManager(getContext());
            eventList.setLayoutManager(llmanager);
            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(getContext(), R.drawable.table_lines);
            eventList.addItemDecoration(itemDecoration);
            eventList.setAdapter(listAdapter);
        }
        return v;
    }

}
