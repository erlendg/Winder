package com.eim.winder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsDSService;
import com.eim.winder.db.LocationDAO;
import com.eim.winder.db.LocationDSService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //private ListView alertList;
    //private ArrayAdapter customListAdapter;
    private LocationDSService datasource;
    private AlertSettingsDSService alertdatasource;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayoutManager llManager;
    private RecyclerView.Adapter rvAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialiserer varsellisten og adapteren som "lytter" på listen:
        //bruker hardkodet liste som midlertidige listeelementer.
        datasource = new LocationDSService(this);
        alertdatasource = new AlertSettingsDSService(this);
        String[] tempListItems = datasource.getArray();
        //Cardview:
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);
        rvAdapter = new RVAdapter(getAlertSettingsDataSet(), new RVAdapter.OnItemClickListener(){
            @Override public void onItemClick(AlertSettingsDAO item) {
                Log.i(TAG, " " +item.getLocation().getName());
            }
        });
        recyclerView.setAdapter(rvAdapter);
        //customListAdapter = new CustomArrayAdapter(this, tempListItems);
        //alertList = (ListView) findViewById(R.id.alert_listview);
        //alertList.setAdapter(customListAdapter);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAlertSettingsActivity(view);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
            }
        });

    }
    public void startAlertSettingsActivity(View v){
        Intent intent = new Intent(this, AlertSettingsActivity.class);
        startActivityForResult(intent, 1);
    }
    private ArrayList<AlertSettingsDAO> getAlertSettingsDataSet() {
        Log.i(TAG, "getAlertSettingsDataSet()");
        ArrayList<AlertSettingsDAO> results = alertdatasource.getAllAlertSettings();
        if(results != null || results.size() == 0){
            Log.i(TAG, "getAlertSettingsDataSet() IFIFIFIF "+ results.size());
            for(int i = 0; i < results.size(); i++){
                int id = (int) results.get(i).getLocation().getId();
                Log.i(TAG, "getAlertSettingsDataSet() IFIFIFIF "+ id);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
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
