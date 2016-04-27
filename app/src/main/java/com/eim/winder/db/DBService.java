package com.eim.winder.db;

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
        Log.i(TAG, "getAlertSettingsAndLocation()");
        ArrayList<AlertSettings> results = alertDataSource.getAllAlertSettings();
        if(results != null && results.size() > 0){
            Log.i(TAG, "getAlertSettingsDataSet() Data size: "+ results.size());
            //numOfLocations= results.size();
            for(int i = 0; i < results.size(); i++){
                int id = (int) results.get(i).getLocation().getId();
                Location loc =  locationDataSource.getLocationFromID(id);
                results.get(i).setLocation(loc);
            }
        }else {
            results = Locations.getTestAlertList();
        }
        return results;
    }
    public AlertSettings getCompleteAlertSettingsById(int id){
        Log.i(TAG, "getCompleteAlertSettingsById()");
        AlertSettings result= alertDataSource.getAlertSettingById(id);

        //finner Location-navn basert på id:
        Location loc  = locationDataSource.getLocationFromID((int)result.getLocation().getId());
        result.setLocation(loc);
        return result;
    }
    public boolean deleteAlertSettingAndForecasts(int alertID){
        forecastDataSource.deleteForecastByAlertSettingsID(alertID);
        return alertDataSource.deleteAlertSettings(alertID);
    }
    public ArrayList<Location> getAllLocations(){
        return locationDataSource.getAllLocations();
    }

    public long addAlertSettings(AlertSettings alertSettings){
        return alertDataSource.insertAlertSettings(alertSettings);
    }

    public boolean updateAlertSettings(AlertSettings alertSettings){
        long ok = alertDataSource.updateAlertSettings(alertSettings);
        if((int) ok != 0){
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
