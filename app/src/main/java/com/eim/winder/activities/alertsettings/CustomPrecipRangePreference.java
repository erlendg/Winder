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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Mari on 05.04.2016.
 */
public class CustomPrecipRangePreference extends Preference{
    private static final double DEFAULT_MIN_VALUE= 0.0;
    private static final double DEFAULT_MAX_VALUE= 30.0;
    private RangeSeekBar rsb;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "prefValuesSaved";
    private static final String MIN_PRECIP = "minPrecip";
    private static final String MAX_PRECIP = "maxPrecip";

    public CustomPrecipRangePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayoutResource(R.layout.precippref_layout);
    }
    @Override
    protected View onCreateView(ViewGroup parent) {
        View v = super.onCreateView(parent);
        rsb = (RangeSeekBar) v.findViewById(R.id.precipRangeBar);
        prefs = getContext().getSharedPreferences(PREF_NAME, getContext().MODE_PRIVATE);
        //Log.i("PrecipSaved", getDouble(prefs, MIN_PRECIP, DEFAULT_MIN_VALUE) + ", " + getDouble(prefs, MAX_PRECIP, DEFAULT_MAX_VALUE));
        if(rsb != null ) {
            rsb.setSelectedMinValue(getDouble(prefs, MIN_PRECIP, DEFAULT_MIN_VALUE));
            rsb.setSelectedMaxValue(getDouble(prefs, MAX_PRECIP, DEFAULT_MAX_VALUE));
        }
        return v;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        rsb = (RangeSeekBar) view.findViewById(R.id.precipRangeBar);
        rsb.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                double max = rsb.getSelectedMaxValue().doubleValue();
                double min = rsb.getSelectedMinValue().doubleValue();
                SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                putDouble(editor, MIN_PRECIP, min);
                putDouble(editor, MAX_PRECIP, max);
                editor.commit();
                rsb.setSelectedMinValue(getDouble(prefs, MIN_PRECIP, DEFAULT_MIN_VALUE));
                rsb.setSelectedMaxValue(getDouble(prefs, MAX_PRECIP, DEFAULT_MAX_VALUE));
               // Log.d("SAVE", min + ", " + getDouble(prefs, MIN_PRECIP, DEFAULT_MIN_VALUE) + ", " + getDouble(prefs, MAX_PRECIP, DEFAULT_MAX_VALUE));
            }
        });
    }

    //Help-methods to store double-values in SharedPreferences (no default method)
    public SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
    public static double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        if ( !prefs.contains(key))
            return defaultValue;
        double res = Double.longBitsToDouble(prefs.getLong(key, 0));
        DecimalFormat df = new DecimalFormat("##.0");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        double num = Double.parseDouble(df.format(res));
        return num;
    }
}
