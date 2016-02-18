package com.eim.winder;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eim.winder.db.AlertSettingsDAO;

import java.util.ArrayList;

/**
 * Created by Mari on 18.02.2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.WeatherViewHolder>{
    private static String TAG = "RVAdapter";
    private ArrayList<AlertSettingsDAO> alertsettings;
    //private static MyClickListener myClickListener;

    RVAdapter(ArrayList<AlertSettingsDAO> alertsettings){
        this.alertsettings = alertsettings;
    }
    //implements View.OnClickListener
    public static class WeatherViewHolder extends RecyclerView.ViewHolder  {
        CardView card_view;
        TextView locationName;
        TextView weatherInfo;
        ImageView weatherIcon;


        WeatherViewHolder(View itemView) {
            super(itemView);
            card_view = (CardView)itemView.findViewById(R.id.card_view);
            locationName = (TextView)itemView.findViewById(R.id.location_name);
            weatherInfo = (TextView)itemView.findViewById(R.id.weather_info);
            weatherIcon = (ImageView)itemView.findViewById(R.id.weather_photo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View itemView){
                    Log.i(TAG, " Clicked on item!");
                }
            });
        }

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
    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.locationName.setText(alertsettings.get(position).getLocation().getName());
        holder.weatherInfo.setText("Check interval:" +alertsettings.get(position).getCheckInterval());
        holder.weatherIcon.setImageResource(R.drawable.testicon);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    /*public interface MyClickListener {
        public void onItemClick(int position, View v);
    }*/
}
