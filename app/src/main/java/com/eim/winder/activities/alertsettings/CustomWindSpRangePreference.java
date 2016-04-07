package com.eim.winder.activities.alertsettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.eim.winder.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by Mari on 05.04.2016.
 */
public class CustomWindSpRangePreference extends Preference {
    private static final int DEFAULT_MIN_VALUE= 0;
    private static final int DEFAULT_MAX_VALUE= 40;
    private RangeSeekBar rsb;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "prefValuesSaved";
    private static final String MIN_WS = "minWindSpeed";
    private static final String MAX_WS= "maxWindSpeed";

    public CustomWindSpRangePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayoutResource(R.layout.windspeedpref_layout);
    }
    @Override
    protected View onCreateView(ViewGroup parent) {
        View v = super.onCreateView(parent);
        rsb = (RangeSeekBar) v.findViewById(R.id.windSpeedRangeBar);
        prefs = getContext().getSharedPreferences(PREF_NAME, getContext().MODE_PRIVATE);
        int min = prefs.getInt(MIN_WS, DEFAULT_MIN_VALUE);
        int max = prefs.getInt(MAX_WS, DEFAULT_MAX_VALUE);
       // Log.i("WindSpeedSaved", min + ", " + max);
        if(rsb != null ) {
            rsb.setSelectedMinValue(min);
            rsb.setSelectedMaxValue(max);
        }
        return v;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        rsb = (RangeSeekBar) view.findViewById(R.id.windSpeedRangeBar);
        rsb.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                int min = rsb.getSelectedMinValue().intValue();
                int max = rsb.getSelectedMaxValue().intValue();
                SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(MIN_WS, min);
                editor.putInt(MAX_WS, max);
                editor.commit();
               // Log.d("SAVE", min + ", " + max);
            }
        });
    }
}
