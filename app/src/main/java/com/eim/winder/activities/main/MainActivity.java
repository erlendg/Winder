package com.eim.winder.activities.main;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eim.winder.activities.alertoverview.AlertOverViewActivity;
import com.eim.winder.activities.appsettings.UserSettingsActivity;
import com.eim.winder.activities.selectlocation.SelectLocationActivity;
import com.eim.winder.db.DBService;
import com.eim.winder.xml.CompareAXService;
import com.eim.winder.xml.HandleXML;
import com.eim.winder.R;
import com.eim.winder.db.AlertSettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int MAX_LOCATIONS = 10;
    private static final int RESULT_SETTINGS = 1;
    private static boolean isActivityRunning = false;

    private static MainActivity instance;
    private static Locale systemDefaultLanguage;
    private DBService dbService;

    private ArrayList<AlertSettings> alertSettingsList;
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
        //Set application language:
        systemDefaultLanguage = Locale.getDefault();
        setApplicationLocale(systemDefaultLanguage, this);
        setContentView(R.layout.activity_main);
        isActivityRunning = true;
        instance = this;



        //Initiates the datasource:
        dbService = new DBService(this);
        buildViewComponents(this);

    }
    private void buildViewComponents( Context context){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_stat_name);
        //Cardview:Initiates the list with locationalerts and the adapter that "listens" on the list:
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        alertSettingsList = getLocationsAndAlertData();
        llManager = new LinearLayoutManager(context);
        buildRecyclerView(recyclerView, llManager, alertSettingsList);

    }

    private void buildRecyclerView(RecyclerView recyclerView, LinearLayoutManager llManager, ArrayList<AlertSettings> alertSettingsList){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llManager);
        setRvAdapter(recyclerView, alertSettingsList);

    }
    private void setRvAdapter(RecyclerView recyclerView, ArrayList<AlertSettings> alertSettingsList){
        rvAdapter = new RVAdapter(this, alertSettingsList, new RVAdapter.OnItemClickListener(){
            @Override public void onItemClick(AlertSettings item) {
                Log.i(TAG, " " +item.getLocation().getName());
                startAlertOverViewActivity(item);

            }
        });
        recyclerView.setAdapter(rvAdapter);
    }
    public ArrayList<AlertSettings> getLocationsAndAlertData(){
        alertSettingsList = dbService.getAllAlertSettingsAndLocations();
        LinearLayout emptyListReplacer = (LinearLayout) findViewById(R.id.empty_list_replacer);
        emptyListReplacer.setVisibility(View.VISIBLE);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_activity_content);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(alertSettingsList.isEmpty()){
            emptyListReplacer = (LinearLayout) findViewById(R.id.empty_list_replacer);
            emptyListReplacer.setVisibility(View.VISIBLE);
            //layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorEmptyList));
            setFabAnimation(fab);
        }else {
            emptyListReplacer = (LinearLayout) findViewById(R.id.empty_list_replacer);
            emptyListReplacer.setVisibility(View.GONE);
            //layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRecycleViewBackground));
            fab.clearAnimation();
        }
        return alertSettingsList;
    }
    private void setFabAnimation(FloatingActionButton fab){
        final Animation animation = new AlphaAnimation(1.0f, 0.3f); // Change alpha from fully visible to invisible
        animation.setDuration(700); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        fab.startAnimation(animation);

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

    public void startAlertOverViewActivity(AlertSettings asd){
        Log.i(TAG, "---> startAlertOverViewActivity");
        Intent intent = new Intent(this, AlertOverViewActivity.class);
        intent.putExtra("AlertSettings", asd);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.pull_in_right_anim, R.anim.push_out_left_anim).toBundle();
        startActivity(intent, bndlanimation);
    }
    public ArrayList<AlertSettings> getRecycleViewDataset(){
        return dbService.getAllAlertSettingsAndLocations();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    @Override
    protected void onResume() {
        //Updates the view in case of changes in the alertlist
        super.onResume();
        Locale l = getResources().getConfiguration().locale;
        Log.i(TAG, l.getLanguage());
        if(!isActivityRunning) {
            //Log.i(TAG, "onResume()");
            alertSettingsList = getLocationsAndAlertData();
            setRvAdapter(recyclerView, alertSettingsList);
            isActivityRunning = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                doManualForecastRefresh();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Intent i = new Intent(this, UserSettingsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }

    public void doManualForecastRefresh(){
        Toast.makeText(this, "Updating forecasts!", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < alertSettingsList.size(); i++) {
            //create an instance of CompareAXService:
            compare = new CompareAXService(this, alertSettingsList.get(i));
            //run the xml-parser:
            div = compare.runHandleXML();
            int compareResult;
            //if the parsing is done, run findAllOccurences:
            if(div) {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                compareResult = compare.findAllOccurences(alertSettingsList.get(i).getId(), alertSettingsList.get(i).getLocation().getName(), this, this.getClass(), mNotificationManager);
                       /* if (!listeTing.isEmpty()) {
                            compare.generateNotification(listeTing, temp.getId(), this, this.getClass(), mNotificationManager);
                        }*/
                String lastUpdate = compare.getTimeAndStoreIt(getResources().getConfiguration().locale);
                if(compareResult == 1 || compareResult ==3 )notifyAlertSettingsListChanged(i, 1, lastUpdate);
                else notifyAlertSettingsListChanged(i, 0, lastUpdate);
                //Si ifra til adapteren med arraylisten av alertsettings at det har skjedd en endring:

            }
            //Toast.makeText(this, "Alertsetting " + alertSettingsList.get(i).getId(), Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "All done!", Toast.LENGTH_SHORT).show();
    }

    public static void setApplicationLocale(Locale l, Context context) {
        //Setter den til norsk hvis det er satt på enheten ved oppstart:
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedLanguage = sp.getString("prefLanguage", "default");
        Log.i(TAG, "Selected language from sharedPreferences: " + selectedLanguage);
        if (selectedLanguage.equalsIgnoreCase("en")) {
            l = new Locale("en", "en_US");
        } else if (selectedLanguage.equalsIgnoreCase("no")){
            l = new Locale("no", "NO");
        } else{
            if (selectedLanguage.equalsIgnoreCase("default")) {
                l = systemDefaultLanguage;
            }
            if (l.getLanguage().equals("no") || l.getLanguage().equals("nb") || l.getLanguage().equals("nn") || l.getLanguage().equals("nb-no")) {
                l = new Locale("no", "NO");
                //hvis ikke norsk så settes den til engelsk ved oppstart:
            } else {
                l = new Locale("en", "en_US");
            }
        }
        Configuration config = new Configuration();
        config.locale = l;
        Resources res = context.getResources();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public void notifyAlertSettingsListChanged(int alertListId, int colorID, String lastUpdate){
        AlertSettings div = alertSettingsList.get(alertListId);
        div.setHasEvents(colorID);
        div.setLastUpdate(lastUpdate);
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }

    public int getNumOfLocations(){
        return alertSettingsList.size();
    }

    public static MainActivity  getInstance(){
        return instance;
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

    @Override
    protected void onPause() {
        super.onPause();
        //Log.wtf(TAG, "pause");
        isActivityRunning = false;
    }
    public static boolean getIsActivityRunning(){
        return isActivityRunning;
    }

}
