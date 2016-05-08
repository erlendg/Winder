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
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.eim.winder.activities.alertoverview.AlertOverViewActivity;
import com.eim.winder.activities.appsettings.UserSettingsActivity;
import com.eim.winder.activities.selectlocation.SelectLocationActivity;
import com.eim.winder.db.DBService;
import com.eim.winder.xml.CompareAXService;
import com.eim.winder.R;
import com.eim.winder.db.AlertSettings;

import java.util.ArrayList;
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

    /**
     * Creates the MainActivity with content view activity_main.xml and the language for the app.
     * Also initiates database service-object: DBService and builds the view components.
     * @param savedInstanceState
     */

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

    /**
     * Builds toolbar, and the recycleview list with cards for the weather alerts the user adds.
     * @param context needs the context to build the view.
     */
    private void buildViewComponents( Context context){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_stat_name);
        //Cardview:Initiates the list with location alerts and the adapter that "listens" for changes on the list:
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        alertSettingsList = getLocationsAndAlertData();
        llManager = new LinearLayoutManager(context);
        buildRecyclerView(recyclerView, llManager, alertSettingsList);

    }

    /**
     * Builds the recycleview view or the main weather alert list.
     * @param recyclerView the list itself
     * @param llManager Manager of the list
     * @param alertSettingsList Objects inside the list
     */
    private void buildRecyclerView(RecyclerView recyclerView, LinearLayoutManager llManager, ArrayList<AlertSettings> alertSettingsList){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llManager);
        setRvAdapter(recyclerView, alertSettingsList);

    }

    /**
     * Sets the custom RVAdaper (list adapter) on the weather alert list
     * @param recyclerView view that needs an adapter
     * @param alertSettingsList list of objects inside it.
     */
    private void setRvAdapter(RecyclerView recyclerView, ArrayList<AlertSettings> alertSettingsList){
        rvAdapter = new RVAdapter(this, alertSettingsList, new RVAdapter.OnItemClickListener(){
            @Override public void onItemClick(AlertSettings item) {
                Log.i(TAG, " " +item.getLocation().getName());
                //Starts an OverviewActivity if item gets clicked on.
                startAlertOverViewActivity(item);

            }
        });
        recyclerView.setAdapter(rvAdapter);
    }

    /**
     * Fetches all the Alertsettings objects the user has saved.
     * @return An ArrayList of Alertsettings objects that the user has added weather alerts for.
     */
    public ArrayList<AlertSettings> getLocationsAndAlertData(){
        alertSettingsList = dbService.getAllAlertSettingsAndLocations();
        LinearLayout emptyListReplacer = (LinearLayout) findViewById(R.id.empty_list_replacer);
        emptyListReplacer.setVisibility(View.VISIBLE);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(alertSettingsList.isEmpty()){
            //If no locations for weather alerts are stored then show an animation
            emptyListReplacer = (LinearLayout) findViewById(R.id.empty_list_replacer);
            emptyListReplacer.setVisibility(View.VISIBLE);
            setFabAnimation(fab);
        }else {
            // If the list is not empty, then clear animation and hide the greeting text.
            emptyListReplacer = (LinearLayout) findViewById(R.id.empty_list_replacer);
            emptyListReplacer.setVisibility(View.GONE);
            fab.clearAnimation();
        }
        return alertSettingsList;
    }

    /**
     * Sets animation on a FloatingActionAutton (blue + button)
     * @param fab the button that needs animation
     */
    private void setFabAnimation(FloatingActionButton fab){
        final Animation animation = new AlphaAnimation(1.0f, 0.3f); // Change alpha from fully visible to invisible
        animation.setDuration(700); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        fab.startAnimation(animation);
    }

    /**
     * Starts the SelectLocationActivity so the user can add a new location for weather alerts.
     * Only starts if the size of the AlertSettings list is less or equal to 10.
     * Currently there is only allowed to register 10 locations for alert to limit dataflow.
     * @param v the view of the action that initialize this method. (MainActivity)
     */
    public void startSelectLocationActivity(View v){
        //Starts the new Activity if there is less then 10 items in the list.
        if(getNumOfLocations() != MAX_LOCATIONS){
            Intent intent = new Intent(this, SelectLocationActivity.class);
            Log.i(TAG, "---> startSelectLocationActivity");
            startActivity(intent);
        }else{
            Snackbar.make(v, getString(R.string.toast_more_then_ten_alerts), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    /**
     * Starts the startAlertOverViewActivity and sends the selected AlertSettings with it.
     * Also puts a natural left to right slide animation on the change of views.
     * @param asd The clicked Alertsettings objects that the user wants to see the details for.
     */
    public void startAlertOverViewActivity(AlertSettings asd){
        Log.i(TAG, "---> startAlertOverViewActivity");
        Intent intent = new Intent(this, AlertOverViewActivity.class);
        intent.putExtra("AlertSettings", asd);
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.pull_in_right_anim, R.anim.push_out_left_anim).toBundle();
        startActivity(intent, bundleAnimation);
    }

    /**
     * Returns the list of the current list of saved AlertSettings (or locations for weather check and there settings)
     * For the AlarmReceiver class and test classes only.
     * @return ArrayList of AlertSettings
     */
    public ArrayList<AlertSettings> getRecycleViewDataset(){
        return dbService.getAllAlertSettingsAndLocations();
    }

    /**
     * When resuming to MainActivity the list of AlertSettings objects gets updated, update the language if needed
     * and sets the isActivityRunning = true, so that if the AlarmReceiver gets called, it knows that the main view is open.
     */
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

    /**
     * Creates the app menu.
     * @param menu The menu object.
     * @return true if menu was successfully inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Puts items/elements inside menu. The menu has two menu items: refresh the list and go to settings.
     * If clicked on the settings icon the UserSettingsActivivty gets started.
     * @param item
     * @return true if menu items was successfully added.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Refresh the forecast manually:
            case R.id.action_refresh:
                doManualForecastRefresh();
                break;
            // Starts the UserSettingsActivity:
            case R.id.action_settings:
                Intent i = new Intent(this, UserSettingsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Manually refreshes the forecast to the AlertSettings objects
     */
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

    /**
     * Sets the application locale based on default system settings or the selected settings
     * from the UserSettingsActivity view view.
     * @param l the Locale value.
     * @param context needs context to get SharedPreferences object.
     */
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
            if (selectedLanguage.equalsIgnoreCase("default") && systemDefaultLanguage != null) {
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

    /**
     * Extension of the default Adapter.notifyDataSetChanged to update a specific item in the list
     * if there is new weather events. It updates the color of the item and the field "last update".
     * Green color = the location has weather events matching the selected settings.
     * Blue color = the location has no current events matching the selected settings.
     * @param alertListId the id of the AlertSettings.
     * @param colorID   the new color id of the event.
     * @param lastUpdate the new last update for the item.
     */
    public void notifyAlertSettingsListChanged(int alertListId, int colorID, String lastUpdate){
        AlertSettings div = alertSettingsList.get(alertListId);
        div.setHasEvents(colorID);
        div.setLastUpdate(lastUpdate);
        rvAdapter.notifyDataSetChanged();
    }

    /**
     * If any app configurations changes this method gets called.
     * @param newConfig the new configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }

    /**
     *
     * @return the size of the AlertSettings list. for
     */

    public int getNumOfLocations(){
        return alertSettingsList.size();
    }

    /**
     * @return The instance of the MainActivity. Only used in AlarmReceiver and test.
     */
    public static MainActivity  getInstance(){
        return instance;
    }
    /**
     * Updates the value of isActivity running so the AlarmReceiver knows if the
     * Activity is running or not.
     */
    @Override
    protected void onPause() {
        super.onPause();
        //Log.wtf(TAG, "pause");
        isActivityRunning = false;
    }

    /**
     *
     * @return true if the MainActivity  is running. Used by AlarmReceiver.
     */
    public static boolean getIsActivityRunning(){
        return isActivityRunning;
    }

}
