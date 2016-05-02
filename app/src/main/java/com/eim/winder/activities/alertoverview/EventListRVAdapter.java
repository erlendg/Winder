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
 */
public class EventListRVAdapter extends RecyclerView.Adapter<EventListRVAdapter.WeatherViewHolder> {
    private static String TAG = "EventListRVAdapter";
    private ArrayList<Forecast> forecastList;

    //Constructor receives an object that implements the listener interface, along with items
    EventListRVAdapter(ArrayList<Forecast> forecastList){
        this.forecastList = forecastList;
    }

    /*public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
    public MyRecyclerViewAdapter(ArrayList<AlertSettings> alertsettings) {
        mDataset = myDataset;
    }*/
    //This method is called when the custom ViewHolder needs to be initialized.
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

    //Specifies the contents of each item of the RecyclerView
    //The ViewHolder will receive the constructor in the custom bind method
    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        //String date = forecastList.get(position).getFormatedDate();

        holder.eventDate.setText(fixDate(forecastList.get(position).getFormatedDate()));
        holder.eventDescription.setText(forecastList.get(position).getFormatedInfo());
    }
    private String fixDate(String date){
        int length = date.length();
        date = date.substring(0, length-18) + date.substring( length-15, length);
        return date;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    /*public interface MyClickListener {
        public void onItemClick(int position, View v);
    }*/
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
