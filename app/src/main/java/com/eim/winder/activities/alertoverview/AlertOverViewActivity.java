package com.eim.winder.activities.alertoverview;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.eim.winder.R;
import com.eim.winder.activities.alertsettings.AlertSettingsActivityBeta;
import com.eim.winder.databinding.ActivityAlertOverViewBinding;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsDSService;
import com.eim.winder.db.LocationDAO;
import com.eim.winder.scheduler.AlarmReceiver;

public class AlertOverViewActivity extends AppCompatActivity {
    private final String TAG = "AlertOverWievActivity";
    private AlertSettingsDAO alertSettingsDAO;
    private LocationDAO location;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView preferencesTitle;
    private GridLayout preferencesTable;
    private AlertSettingsDSService datasource;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_over_view);
        //Gets the object that was sent from MainActivity
        Bundle bundle = getIntent().getExtras();
        alertSettingsDAO = bundle.getParcelable("AlertSettingsDAO");
        location = alertSettingsDAO.getLocation();
        //Opens datasource usage:
        datasource = new AlertSettingsDSService(this);
        //Enables databinding to the xml-layout:
        ActivityAlertOverViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_alert_over_view);
        //Enables toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Enables back-button:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new EventListFragment(), getString(R.string.eventlist));
        viewPagerAdapter.addFragment(new SettingsFragment(), getString(R.string.settings));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        //Sets the toolbar name to the location name (doesn't work in xml)
        collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(alertSettingsDAO.getLocation().getName());
        // Sets the action of the blue floating action button:
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //Sets the onclick-listener to the preference-title field:
        preferencesTitle = (TextView) findViewById(R.id.preferences_title);
        preferencesTable = (GridLayout) findViewById(R.id.preferences_table);

        /*preferencesTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesTable.setVisibility(preferencesTable.isShown() ? View.GONE : View.VISIBLE );

            }
        });*/
    }
    //Sets the onclick-listener to the preference-title field:
    public void onDeleteButtonClick(View v){
        final int alertId = alertSettingsDAO.getId();
        new AlertDialog.Builder(this)
                .setMessage("Do you want to delete the alert?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (datasource.deleteAlertSettings(alertSettingsDAO.getId())) {
                            finish();
                            cancelAlarm(alertId);
                            Toast.makeText(AlertOverViewActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AlertOverViewActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
    public AlertSettingsDAO getAlertSettingsDAO(){
        return alertSettingsDAO;
    }


    /**
     * Method to cancel a currently scheduler AlarmManager alarm based on the original Intent's id.
     * @param id id of the desired AlertSetting, and subsequently Intent.
     */
    public void cancelAlarm(int id){
        Log.e(TAG,"cancelAlarm: "+ id );
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent toDo = PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(toDo);

    }
    public void onEditAlertButtonClick(View v){
        Intent intent = new Intent(this, AlertSettingsActivityBeta.class);
        intent.putExtra("LocationDAO", alertSettingsDAO.getLocation());
        intent.putExtra("edit", true);
        intent.putExtra("alertID", alertSettingsDAO.getId());
        Log.i(TAG, "---> startAlertSettingsActivity");
        startActivity(intent);
        finish();
    }
}
