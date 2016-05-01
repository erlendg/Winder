package com.eim.winder.db;

import android.content.Context;
import android.util.Log;

import com.eim.winder.div.Locations;

import java.util.ArrayList;

/**
 * Created by Mari on 20.04.2016.
 */
public class DBService {

    private static final String TAG = "DBService";
    private AlertSettingsRepo alertDataSource;
    private LocationRepo locationDataSource;
    private ForecastRepo forecastDataSource;

    public DBService(){}

    public DBService(Context context){
        this.locationDataSource = new LocationRepo(context);
        this.alertDataSource = new AlertSettingsRepo(context);
        this.forecastDataSource = new ForecastRepo(context);
    }

    public DBService(AlertSettingsRepo alertDataSource, LocationRepo locationDataSource, ForecastRepo forecastDataSource) {
        this.alertDataSource = alertDataSource;
        this.locationDataSource = locationDataSource;
        this.forecastDataSource = forecastDataSource;
    }

    public DBService(AlertSettingsRepo alertDataSource, LocationRepo locationDataSource) {
        this.alertDataSource = alertDataSource;
        this.locationDataSource = locationDataSource;
    }

    public DBService(AlertSettingsRepo alertDataSource, ForecastRepo forecastDataSource) {
        this.alertDataSource = alertDataSource;
        this.forecastDataSource = forecastDataSource;
    }

    public DBService(AlertSettingsRepo alertDataSource) {
        this.alertDataSource = alertDataSource;
    }

    public DBService(LocationRepo locationDataSource) {
        this.locationDataSource = locationDataSource;
    }

    public DBService(ForecastRepo forecastDataSource) {
        this.forecastDataSource = forecastDataSource;
    }

    public ArrayList<AlertSettings> getAllAlertSettingsAndLocations() {
        ArrayList<AlertSettings> results = alertDataSource.getAllAlertSettings();
        if(results != null && results.size() > 0){
            Log.i(TAG, "getAlertSettingsAndLocation() Data size: "+ results.size());
            //numOfLocations= results.size();
            for(int i = 0; i < results.size(); i++){
                int id = (int) results.get(i).getLocation().getId();
                Location loc =  locationDataSource.getLocationFromID(id, alertDataSource.getReadDB());
                results.get(i).setLocation(loc);
            }
        }else {
            Log.i(TAG, "getAlertSettingsAndLocation() Data size: "+ 0);
            results = Locations.getTestAlertList();
        }
        locationDataSource.close();
        return results;
    }
    public AlertSettings getCompleteAlertSettingsById(int id){
        Log.i(TAG, "getCompleteAlertSettingsById()");
        AlertSettings result= alertDataSource.getAlertSettingById(id);
        //Finds Location-name based på id:
        Location loc  = locationDataSource.getLocationFromID((int)result.getLocation().getId(), alertDataSource.getReadDB());
        result.setLocation(loc);
        alertDataSource.close();
        return result;
    }
    public boolean deleteAlertSettingAndForecasts(int alertID){
        Log.i(TAG, "deleteAlertSettingAndForecasts("+ alertID +")");
        forecastDataSource.deleteForecastByAlertSettingsID(alertID);
        return alertDataSource.deleteAlertSettings(alertID);
    }
    public ArrayList<Location> getAllLocations(){
        Log.i(TAG, "getAllLocations()");
        return locationDataSource.getAllLocations();
    }

    public long addAlertSettings(AlertSettings alertSettings){
        Log.i(TAG, "addAlertSettings()");
        return alertDataSource.insertAlertSettings(alertSettings);
    }

    public boolean updateAlertSettings(AlertSettings alertSettings){
        long ok = alertDataSource.updateAlertSettings(alertSettings);
        if((int) ok != 0 && (int) ok != -1){
            Log.i(TAG, "updateAlertSettings() updated: "+ ok);
            return true;
        }else{
            Log.e(TAG, "updateAlertSettings() failed due to value: " +ok);
            return false;
        }
    }
    public boolean addForecastList(ArrayList<Forecast> forecasts, int alertId){
        return forecastDataSource.insertForecastList(forecasts, alertId);
    }
}
