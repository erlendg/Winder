package com.eim.winder.activities.main;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
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

import com.eim.winder.activities.alertoverview.AlertOverViewActivity;
import com.eim.winder.activities.selectlocation.SelectLocationActivity;
import com.eim.winder.db.DBService;
import com.eim.winder.xml.CompareAXService;
import com.eim.winder.xml.HandleXML;
import com.eim.winder.R;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsRepo;
import com.eim.winder.db.LocationRepo;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int MAX_LOCATIONS = 10;

    private DBService dbService;
    private LocationRepo locationDataSource;
    private AlertSettingsRepo alertDataSource;

    private ArrayList<AlertSettingsDAO> alertSettingsList;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayoutManager llManager;
    private RecyclerView.Adapter rvAdapter;
    private CompareAXService compare;
    private boolean div = false;

    private boolean div2;
    private NotificationCompat.Builder notification;
    private HandleXML xmlhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_stat_name);

        //Set application language:
        setApplicationLocale(getResources().getConfiguration().locale);
        Log.e("Locale:", Locale.getDefault().getLanguage());

        //Initiates the datasource:
        locationDataSource = new LocationRepo(this);
        alertDataSource = new AlertSettingsRepo(this);
        dbService = new DBService(alertDataSource, locationDataSource);

        //Cardview:Initiates the list with locationalerts and the adapter that "listens" on the list:
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        alertSettingsList = dbService.getAlertSettingsAndLocation();
        llManager = new LinearLayoutManager(this);
        buildRecyclerView(recyclerView, llManager, alertSettingsList);

        //Floating action button:
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectLocationActivity(view);
            }
        });

    }

    private void buildRecyclerView(RecyclerView recyclerView, LinearLayoutManager llManager, ArrayList<AlertSettingsDAO> alertSettingsList){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llManager);
        setRvAdapter( recyclerView, alertSettingsList);

    }
    private void setRvAdapter(RecyclerView recyclerView, ArrayList<AlertSettingsDAO> alertSettingsList){
        rvAdapter = new RVAdapter(this, alertSettingsList, new RVAdapter.OnItemClickListener(){
            @Override public void onItemClick(AlertSettingsDAO item) {
                Log.i(TAG, " " +item.getLocation().getName());
                startAlertOverViewActivity(item);

            }
        });
        recyclerView.setAdapter(rvAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    @Override
    protected void onResume() {
        //Updates the view in case of changes in the alertlist
        super.onResume();
        alertSettingsList = dbService.getAlertSettingsAndLocation();
        setRvAdapter(recyclerView, alertSettingsList);
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
        // as you specify template_selected_shape parent activity in AndroidManifest.xml.
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
                doManualForecastRefresh();

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
    public void doManualForecastRefresh(){
        Toast.makeText(this, "Refreshing forecast...", Toast.LENGTH_SHORT).show();
        for (AlertSettingsDAO temp : alertSettingsList) {
            //create an instance of CompareAXService:
            compare = new CompareAXService(this, temp);
            //run the xml-parser:
            div = compare.runHandleXML();
            int compareResult;
            //if the parsing is done, run findAllOccurences:
            if(div) {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                compareResult = compare.findAllOccurences(temp.getId(), temp.getLocation().getName(), this, this.getClass(), mNotificationManager);
                       /* if (!listeTing.isEmpty()) {
                            compare.generateNotification(listeTing, temp.getId(), this, this.getClass(), mNotificationManager);
                        }*/
            }
            Toast.makeText(this, "Alertsetting " + temp.getId(), Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "... done!", Toast.LENGTH_SHORT).show();
    }

    public void setApplicationLocale(Locale l) {
        //Setter den til norsk hvis det er satt på enheten ved oppstart:
        if (l.getLanguage().equals("no") || l.getLanguage().equals("nb") || l.getLanguage().equals("nn") || l.getLanguage().equals("nb-no")){
            l = new Locale("no","NO");
            //hvis ikke norsk så settes den til britisk ved oppstart:
        }else{
            l = new Locale("en","en_US");
        }
        Configuration config = new Configuration();
        config.locale = l;
        Resources res = getBaseContext().getResources();
        res.updateConfiguration(config, res.getDisplayMetrics());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }

    public int getNumOfLocations(){
        return alertSettingsList.size();
    }
/**
 * This method is moved to another place in the code:
    private void generateNotification(ArrayList<String> template_selected_shape, int i){
        notification = new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.drawable.testicon);
        if(!template_selected_shape.isEmpty()) {
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

}
