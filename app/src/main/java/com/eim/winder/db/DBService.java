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

    public DBService(LocationRepo locationDataSource) {
        this.locationDataSource = locationDataSource;
    }

    public DBService(ForecastRepo forecastDataSource) {
        this.forecastDataSource = forecastDataSource;
    }

    public ArrayList<AlertSettingsDAO> getAlertSettingsAndLocation() {
        Log.i(TAG, "getAlertSettingsAndLocation()");
        ArrayList<AlertSettingsDAO> results = alertDataSource.getAllAlertSettings();
        if(results != null && results.size() > 0){
            Log.i(TAG, "getAlertSettingsDataSet() Data size: "+ results.size());
            //numOfLocations= results.size();
            for(int i = 0; i < results.size(); i++){
                int id = (int) results.get(i).getLocation().getId();
                LocationDAO loc =  locationDataSource.getLocationFromID(id);
                results.get(i).setLocation(loc);
            }
        }else {
            results = Locations.getTestAlertList();
        }
        return results;
    }
    public boolean deleteAlertSettingAndForecasts(int alertID){
        forecastDataSource.deleteForecastByAlertSettingsID(alertID);
        return alertDataSource.deleteAlertSettings(alertID);
    }
    public ArrayList<LocationDAO> getAllLocations(){
        return locationDataSource.getAllLocations();
    }

    public long addAlertSettings(AlertSettingsDAO alertSettingsDAO){
        return alertDataSource.insertAlertSettings(alertSettingsDAO);
    }
}
