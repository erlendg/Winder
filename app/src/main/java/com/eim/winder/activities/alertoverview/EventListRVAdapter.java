package com.eim.winder.activities.alertoverview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eim.winder.R;
import com.eim.winder.db.Forecast;

import java.util.ArrayList;

/**
 * Created by Mari on 12.04.2016.
 * Custom Event list adapter for the weather events:
 */
public class EventListRVAdapter extends RecyclerView.Adapter<EventListRVAdapter.WeatherViewHolder> {
    private static String TAG = "EventListRVAdapter";
    private ArrayList<Forecast> forecastList;

    /**
     * Constructor receives an object that implements the listener interface, along with items
     * @param forecastList list of items which is stored in the adapter
     */
    EventListRVAdapter(ArrayList<Forecast> forecastList){
        this.forecastList = forecastList;
    }


    /**
     * This method is called when the custom ViewHolder needs to be initialized.
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_event_list_content, viewGroup, false);
        WeatherViewHolder wvh = new WeatherViewHolder(view);
        return wvh;
    }
    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    /**
     * Specifies the contents of each item of the RecyclerView
     * The ViewHolder will receive the constructor in the custom bind method
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.eventDate.setText(fixDate(forecastList.get(position).getFormatedDate()));
        holder.eventDescription.setText(forecastList.get(position).getFormatedInfo());
    }

    /**
     * Customizes the date format for the user:
     * @param date
     * @return new formated date
     */
    private String fixDate(String date){
        int length = date.length();
        date = date.substring(0, length-18) + date.substring( length-15, length);
        return date;
    }

    /**
     * Attaches the RecycleView to the adapter
     * @param recyclerView view
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Initiates the view components in each item in the RecycleView
     */
    public static class WeatherViewHolder extends RecyclerView.ViewHolder  {
        TextView eventDate;
        TextView eventDescription;

        WeatherViewHolder(View itemView) {
            super(itemView);
            eventDate = (TextView)itemView.findViewById(R.id.event_itemDate);
            eventDescription = (TextView)itemView.findViewById(R.id.event_description);
        }


    }

}
