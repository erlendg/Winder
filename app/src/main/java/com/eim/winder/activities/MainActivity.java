package com.eim.winder.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.eim.winder.activities.alertsettings.AlertSettingsActivityBeta;
import com.eim.winder.activities.alertsettings.SelectLocationActivity;
import com.eim.winder.db.ForecastDAO;
import com.eim.winder.xml.CompareAXService;
import com.eim.winder.xml.HandleXML;
import com.eim.winder.div.Locations;
import com.eim.winder.R;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsDSService;
import com.eim.winder.db.LocationDAO;
import com.eim.winder.db.LocationDSService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int MAX_LOCATIONS = 10;
    private int numOfLocations;
    private LocationDSService datasource;
    private AlertSettingsDSService alertdatasource;
    private ArrayList<AlertSettingsDAO> alertSettingsList;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayoutManager llManager;
    private RecyclerView.Adapter rvAdapter;
    private CompareAXService compare;
    private HandleXML xmlhandler;
    private boolean div = false;
    private boolean div2;
    private NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initiates the datasource:
        datasource = new LocationDSService(this);
        alertdatasource = new AlertSettingsDSService(this);

        //Cardview:Initiates the list with locationalerts and the adapter that "listens" on the list:
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);
        alertSettingsList = getAlertSettingsDataSet();
        rvAdapter = new RVAdapter(alertSettingsList, new RVAdapter.OnItemClickListener(){
            @Override public void onItemClick(AlertSettingsDAO item) {
                Log.i(TAG, " " +item.getLocation().getName());
                startAlertOverViewActivity(item);

            }
        });
        recyclerView.setAdapter(rvAdapter);

        //div for xmlhandling and comparison:

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectLocationActivity(view);
            }
        });

    }

    public void startSelectLocationActivity(View v){
        //Only allowed to register up to 10 locations for alert, to limit dataflow
        if(getNumOfLocations() != MAX_LOCATIONS){
            Intent intent = new Intent(this, SelectLocationActivity.class);
            Log.i(TAG, "---> startSelectLocationActivity");
            startActivityForResult(intent, 1);
        }else{
            Snackbar.make(v, getString(R.string.toast_more_then_ten_alerts), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void startAlertOverViewActivity(AlertSettingsDAO asd){
        Log.i(TAG, "---> startAlertOverViewActivity");
        Intent intent = new Intent(this, AlertOverViewActivity.class);
        intent.putExtra("AlertSettingsDAO", asd);
        startActivity(intent);
    }

    public ArrayList<AlertSettingsDAO> getAlertSettingsDataSet() {
        Log.i(TAG, "getAlertSettingsDataSet()");
        ArrayList<AlertSettingsDAO> results = alertdatasource.getAllAlertSettings();
        if(results != null && results.size() > 0){
            Log.i(TAG, "getAlertSettingsDataSet() Data size: "+ results.size());
            numOfLocations= results.size();
            for(int i = 0; i < results.size(); i++){
                int id = (int) results.get(i).getLocation().getId();
                LocationDAO loc =  datasource.getLocationFromID(id);
                results.get(i).setLocation(loc);
            }
        }else {
            results = Locations.getTestAlertList();
        }
        return results;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    @Override
    protected void onResume() {
        //Updates the view in case of changes in the alertlist
        super.onResume();
        rvAdapter = new RVAdapter(getAlertSettingsDataSet(), new RVAdapter.OnItemClickListener(){
            @Override public void onItemClick(AlertSettingsDAO item) {
                Log.i(TAG, " " +item.getLocation().getName());
                startAlertOverViewActivity(item);
            }
        });
        recyclerView.setAdapter(rvAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        //Gammel Kode:
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
        */
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                Toast.makeText(this, "Refreshing forecast...", Toast.LENGTH_SHORT).show();
                for (AlertSettingsDAO temp : alertSettingsList) {
                    //create an instance of CompareAXService:
                    compare = new CompareAXService(this, temp);
                    //run the xml-parser:
                    div = compare.runHandleXML();
                    //if the parsing is done, run findAllOccurences:
                    if(div) {
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        ArrayList<ForecastDAO> listeTing = compare.findAllOccurences();

                        compare.generateNotification(listeTing, temp.getId(), this, this.getClass(), mNotificationManager);
                    }
                    Toast.makeText(this, "Alertsetting " + temp.getId(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "... done!", Toast.LENGTH_SHORT);
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }

        return true;


    }
    public int getNumOfLocations(){
        return numOfLocations;
    }
/**
 * This method is moved to another place in the code:
    private void generateNotification(ArrayList<String> a, int i){
        notification = new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.drawable.testicon);
        if(!a.isEmpty()) {
            notification.setContentTitle("Vi har en match.");
            notification.setContentText("for område " + i + "!");
        }
        else{
            notification.setContentTitle("Ingen hendelser");
            notification.setContentText("for område " +i+ "!");
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notification.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(i, notification.build());

    }
    */
    /*@Override
    protected void onResume() {
        //datasource.open();
        datasource = new LocationDSService(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }*/

}
