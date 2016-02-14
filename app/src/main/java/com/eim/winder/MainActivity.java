package com.eim.winder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eim.winder.db.LocationDSService;

public class MainActivity extends AppCompatActivity {
    private ListView alertList;
    private ArrayAdapter customListAdapter;
    private LocationDSService datasource;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialiserer varsellisten og adapteren som "lytter" på listen:
        //bruker hardkodet liste som midlertidige listeelementer.
        datasource = new LocationDSService(this);
        String[] tempListItems = datasource.getArray();
        customListAdapter = new CustomArrayAdapter(this, tempListItems);
        alertList = (ListView) findViewById(R.id.alert_listview);
        alertList.setAdapter(customListAdapter);


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
        startActivity(intent);
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
