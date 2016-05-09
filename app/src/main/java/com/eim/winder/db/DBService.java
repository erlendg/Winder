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

    /**
     * Multiple constructors for testing and different use of repo-objects.
     * @param context is needed to initialize database connection.
     */

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

    /**
     * The AlertSettings object gets stored in the database only with the corresponding Location id,
     * therefore the Locations need to be fetched afterwards in LocationRepo based on the id inside
     * the AlertSettings array object.
     * Contains a connection to AlertSettingsRepo and LocationRepo at the same time.
     * The database connection gets sent from AlertSettingsRepo to LocationRepo.
     * This is to avoid opening and closing the database connection multiple times.
     * @return a ArrayList of AlertSettingsObjects containing Location objects.
     */
    public ArrayList<AlertSettings> getAllAlertSettingsAndLocations() {
        ArrayList<AlertSettings> results = alertDataSource.getAllAlertSettings();
        Log.i(TAG, "getAlertSettingsAndLocation() Data size: "+ results.size());
        if(results != null && results.size() > 0){
            for(int i = 0; i < results.size(); i++){
                int id = (int) results.get(i).getLocation().getId();
                //Sends the connecton to LocatonRepo(datasource):
                Location loc =  locationDataSource.getLocationFromID(id, alertDataSource.getReadDB());
                results.get(i).setLocation(loc);
            }
        }
        //closes the database connection:
        locationDataSource.close();
        return results;
    }

    /**
     * The database connection gets sent from AlertSettingsRepo to LocationRepo.
     * This is to avoid opening and closing the database connection multiple times.
     * @param id of needed AlertSettings
     * @return complete AlertSettings object with
     */
    public AlertSettings getCompleteAlertSettingsById(int id){
        Log.i(TAG, "getCompleteAlertSettingsById()");
        AlertSettings result= alertDataSource.getAlertSettingById(id);
        //Finds Location-name based på id:
        Location loc  = locationDataSource.getLocationFromID((int)result.getLocation().getId(), alertDataSource.getReadDB());
        result.setLocation(loc);
        alertDataSource.close();
        return result;
    }

    /**
     * Deletes AlertSettings objects and saved forecasts for this object in database.
     * @param alertID Id of AlertSettings
     * @return true if AlertSettings object was successfully deleted
     */
    public boolean deleteAlertSettingAndForecasts(int alertID){
        Log.i(TAG, "deleteAlertSettingAndForecasts("+ alertID +")");
        forecastDataSource.deleteForecastByAlertSettingsID(alertID);
        return alertDataSource.deleteAlertSettings(alertID);
    }

    /**
     *
     * @return ArrayList of all locations in database
     */
    public ArrayList<Location> getAllLocations(){
        Log.i(TAG, "getAllLocations()");
        return locationDataSource.getAllLocations();
    }

    /**
     * Adds a AlertSettings object to database
     * @param alertSettings new object to be stored
     * @return number of columns affected in database
     */

    public long addAlertSettings(AlertSettings alertSettings){
        Log.i(TAG, "addAlertSettings()");
        return alertDataSource.insertAlertSettings(alertSettings);
    }

    /**
     * Update an already existing AlertSettings object.
     * @param alertSettings Object containing the values to be updated
     * @return true if successfully updated existing object
     */

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

    /**
     * Adds a list of forecasts for a AlertSettings in database
     * @param forecasts the list to be stored
     * @param alertId the id of AlertSettings
     * @return true if successfully stored
     */
    public boolean addForecastList(ArrayList<Forecast> forecasts, int alertId){
        return forecastDataSource.insertForecastList(forecasts, alertId);
    }

    /**
     * For testing purposes only.
     * @param id of the object
     * @return Location object
     */
    public Location getLocationFromId(int id){
        return locationDataSource.getLocationFromID(id);
    }
}
