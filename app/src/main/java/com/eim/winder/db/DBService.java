package com.eim.winder.db;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Mari on 20.04.2016.
 */
public class DBService {

    private static final String TAG = "DBService";
    private AlertSettingsRepo alertSettingsRepo;
    private LocationRepo locationRepo;
    private ForecastRepo forecastRepo;

    public DBService(){}

    /**
     * Multiple constructors for testing and different use of repo-objects.
     * @param context is needed to initialize database connection.
     */

    public DBService(Context context){
        this.locationRepo = new LocationRepo(context);
        this.alertSettingsRepo = new AlertSettingsRepo(context);
        this.forecastRepo = new ForecastRepo(context);
    }

    public DBService(AlertSettingsRepo alertSettingsRepo, LocationRepo locationRepo, ForecastRepo forecastRepo) {
        this.alertSettingsRepo = alertSettingsRepo;
        this.locationRepo = locationRepo;
        this.forecastRepo = forecastRepo;
    }

    public DBService(AlertSettingsRepo alertSettingsRepo, LocationRepo locationRepo) {
        this.alertSettingsRepo = alertSettingsRepo;
        this.locationRepo = locationRepo;
    }

    public DBService(AlertSettingsRepo alertSettingsRepo, ForecastRepo forecastRepo) {
        this.alertSettingsRepo = alertSettingsRepo;
        this.forecastRepo = forecastRepo;
    }

    public DBService(AlertSettingsRepo alertSettingsRepo) {
        this.alertSettingsRepo = alertSettingsRepo;
    }

    public DBService(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public DBService(ForecastRepo forecastRepo) {
        this.forecastRepo = forecastRepo;
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
        ArrayList<AlertSettings> results = alertSettingsRepo.getAllAlertSettings();
        Log.i(TAG, "getAlertSettingsAndLocation() Data size: "+ results.size());
        if(results != null && results.size() > 0){
            for(int i = 0; i < results.size(); i++){
                int id = (int) results.get(i).getLocation().getId();
                //Sends the connecton to LocatonRepo(datasource):
                Location loc =  locationRepo.getLocationFromID(id, alertSettingsRepo.getReadDB());
                results.get(i).setLocation(loc);
            }
        }
        //closes the database connection:
        alertSettingsRepo.close();
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
        AlertSettings result= alertSettingsRepo.getAlertSettingById(id);
        //Finds Location-name based på id:
        Location loc  = locationRepo.getLocationFromID((int)result.getLocation().getId(), alertSettingsRepo.getReadDB());
        result.setLocation(loc);
        alertSettingsRepo.close();
        return result;
    }

    /**
     * Deletes AlertSettings objects and saved forecasts for this object in database.
     * @param alertID Id of AlertSettings
     * @return true if AlertSettings object was successfully deleted
     */
    public boolean deleteAlertSettingAndForecasts(int alertID){
        Log.i(TAG, "deleteAlertSettingAndForecasts("+ alertID +")");
        forecastRepo.deleteForecastByAlertSettingsID(alertID);
        return alertSettingsRepo.deleteAlertSettings(alertID);
    }

    /**
     *
     * @return ArrayList of all locations in database
     */
    public ArrayList<Location> getAllLocations(){
        Log.i(TAG, "getAllLocations()");
        return locationRepo.getAllLocations();
    }

    /**
     * Adds a AlertSettings object to database
     * @param alertSettings new object to be stored
     * @return number of columns affected in database
     */

    public long addAlertSettings(AlertSettings alertSettings){
        Log.i(TAG, "addAlertSettings()");
        return alertSettingsRepo.insertAlertSettings(alertSettings);
    }

    /**
     * Update an already existing AlertSettings object.
     * @param alertSettings Object containing the values to be updated
     * @return true if successfully updated existing object
     */

    public boolean updateAlertSettings(AlertSettings alertSettings){
        long ok = alertSettingsRepo.updateAlertSettings(alertSettings);
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
        return forecastRepo.insertForecastList(forecasts, alertId);
    }

    /**
     * For testing purposes only.
     * @param id of the object
     * @return Location object
     */
    public Location getLocationFromId(int id){
        return locationRepo.getLocationFromID(id);
    }

    /**
     * @param alertID AlertSettings object id
     * @return ArrayList of Forecast objects
     */
    public ArrayList<Forecast> getForecastsById(int alertID){
        return forecastRepo.getAllForecastsByAlertSettingsID(alertID);
    }
}
