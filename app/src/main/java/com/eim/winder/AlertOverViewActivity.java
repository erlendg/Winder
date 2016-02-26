package com.eim.winder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsDSService;
import com.eim.winder.db.LocationDAO;

public class AlertOverViewActivity extends AppCompatActivity {
    private AlertSettingsDAO alertSettingsDAO;
    private LocationDAO location;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView preferencesTitle;
    private GridLayout preferencesTable;
    private TextView preferencesLocation;
    private AlertSettingsDSService datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_over_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        alertSettingsDAO = bundle.getParcelable("AlertSettingsDAO");
        location = alertSettingsDAO.getLocation();
        datasource = new AlertSettingsDSService(this);

        collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(alertSettingsDAO.getLocation().getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        preferencesTitle = (TextView) findViewById(R.id.preferences_title);
        preferencesLocation = (TextView) findViewById(R.id.row1_title);
        preferencesLocation.setText(alertSettingsDAO.getLocation().toString());
        preferencesTable = (GridLayout) findViewById(R.id.preferences_table);
        preferencesTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesTable.setVisibility(preferencesTable.isShown() ? View.GONE : View.VISIBLE );

            }
        });
    }
    public void onDeleteButtonClick(View v){
        new AlertDialog.Builder(this)
                .setMessage("Do you want to delete the alert?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (datasource.deleteAlertSettings(alertSettingsDAO.getId())) {
                            finish();
                            Toast.makeText(AlertOverViewActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AlertOverViewActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}
