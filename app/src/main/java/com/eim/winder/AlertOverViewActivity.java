package com.eim.winder;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.eim.winder.db.AlertSettingsDAO;

public class AlertOverViewActivity extends AppCompatActivity {
    private AlertSettingsDAO alertSettingsDAO;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView preferencesTitle;
    private TableLayout preferencesTable;
    private TextView preferencesLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_over_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        alertSettingsDAO = bundle.getParcelable("AlertSettingsDAO");

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
        preferencesTable = (TableLayout) findViewById(R.id.preferences_table);
        preferencesTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesTable.setVisibility(preferencesTable.isShown() ? View.GONE : View.VISIBLE );

            }
        });
    }
}
