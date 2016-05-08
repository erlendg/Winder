package com.eim.winder.activities.main;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eim.winder.R;
import com.eim.winder.db.AlertSettings;

import java.util.ArrayList;


/**
 * Created by Mari on 18.02.2016.
 * Custom RecycleView adapter for the AlertSettings list.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.WeatherViewHolder>{
    private static String TAG = "RVAdapter";
    private ArrayList<AlertSettings> alertsettings;
    private static OnItemClickListener listener;
    private Context context;

    /**
     * Constructor receives an object that implements the listener interface, along with items
     */

    RVAdapter(Context context, ArrayList<AlertSettings> alertsettings, OnItemClickListener listener){
        this.alertsettings = alertsettings;
        this.listener = listener;
        this.context = context;
    }

    /**
     * Interface that specifies listener’s behaviour
     */
    public interface OnItemClickListener {
        void onItemClick(AlertSettings item);
    }

    /**
     * This method is called when the custom ViewHolder needs to be initialized.
     * Also sets the icon color to green if the AlertSettings has weather events.
     * @param viewGroup the view to be inflated
     * @param viewType
     * @return the built view
     */
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

    /**
     * Specifies the contents of each item of the RecyclerView
     * The ViewHolder will receive the constructor in the custom bind method
     *
     * @param holder the holder of the view
     * @param position position of the item in the list
     */
    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.bind(alertsettings.get(position), listener);
        holder.locationName.setText(alertsettings.get(position).getLocation().getName());
        String lastUpdate = alertsettings.get(position).getLastUpdate();
        //Sets the last update field:
        if (lastUpdate != null) {
            holder.lastUpdate.setText(alertsettings.get(position).getLastUpdate());
        }else {
            holder.lastUpdate.setText(R.string.no_last_update);
        }
        int resID = context.getResources().getIdentifier(alertsettings.get(position).getIconName(), "drawable", context.getPackageName());
        holder.weatherIcon.setImageResource(resID);
        //If there is weather events then mark the item icon green (set selected)
        if(alertsettings.get(position).hasEvents()>0){
            holder.weatherIcon.setSelected(true);
        }
    }

    /**
     * Attaches the recycleview to the content view
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Inner class for the CardView.
     */
    public static class WeatherViewHolder extends RecyclerView.ViewHolder  {
        CardView card_view;
        TextView locationName;
        TextView lastUpdate;
        ImageButton weatherIcon;


        WeatherViewHolder(View itemView) {
            super(itemView);
            card_view = (CardView)itemView.findViewById(R.id.card_view);
            locationName = (TextView)itemView.findViewById(R.id.location_name);
            lastUpdate = (TextView)itemView.findViewById(R.id.check_interval_num);
            weatherIcon = (ImageButton)itemView.findViewById(R.id.weather_photo);
        }
        /**
         * Binds template_selected_shape listener to each item
         */
        public void bind(final AlertSettings item, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v){
                    listener.onItemClick(item);
                }
            });
        }


    }
}
