package com.eim.winder.activities;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettingsDAO;

import java.util.ArrayList;

/**
 * Created by Mari on 18.02.2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.WeatherViewHolder>{
    private static String TAG = "RVAdapter";
    private ArrayList<AlertSettingsDAO> alertsettings;
    private static OnItemClickListener listener;

    //Constructor receives an object that implements the listener interface, along with items
    RVAdapter(ArrayList<AlertSettingsDAO> alertsettings, OnItemClickListener listener){
        this.alertsettings = alertsettings;
        this.listener = listener;
    }

    // Interface that specifies listener’s behaviour
    public interface OnItemClickListener {
        void onItemClick(AlertSettingsDAO item);
    }
    /*public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
    public MyRecyclerViewAdapter(ArrayList<AlertSettingsDAO> alertsettings) {
        mDataset = myDataset;
    }*/
    //This method is called when the custom ViewHolder needs to be initialized.
    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_row, viewGroup, false);
        WeatherViewHolder wvh = new WeatherViewHolder(view);
        return wvh;
    }
    @Override
    public int getItemCount() {
        return alertsettings.size();
    }

    //Specifies the contents of each item of the RecyclerView
    //The ViewHolder will receive the constructor in the custom bind method
    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.bind(alertsettings.get(position), listener);
        holder.locationName.setText(alertsettings.get(position).getLocation().getName());
        holder.checkInterval.setText(""+alertsettings.get(position).getCheckInterval());
        holder.weatherIcon.setImageResource(R.drawable.testicon);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    /*public interface MyClickListener {
        public void onItemClick(int position, View v);
    }*/
    public static class WeatherViewHolder extends RecyclerView.ViewHolder  {
        CardView card_view;
        TextView locationName;
        TextView checkInterval;
        ImageView weatherIcon;


        WeatherViewHolder(View itemView) {
            super(itemView);
            card_view = (CardView)itemView.findViewById(R.id.card_view);
            locationName = (TextView)itemView.findViewById(R.id.location_name);
            checkInterval = (TextView)itemView.findViewById(R.id.check_interval_num);
            weatherIcon = (ImageView)itemView.findViewById(R.id.weather_photo);
        }
        //Binds a listener to each item
        public void bind(final AlertSettingsDAO item, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v){
                    listener.onItemClick(item);
                }
            });
        }


    }
}