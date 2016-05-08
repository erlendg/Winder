package com.eim.winder.activities.alertsettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.eim.winder.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by Mari on 05.04.2016.
 * NOTE:
 * One custom class for each rangebar preference.
 * This is due to the the method onCreateView which is called every time an item on the PreferenceScreen
 * changes, the default PreferenceScreen object such as CheckBoxPreference saves there value by default but
 * these custom preferences needs to restore there values for every click on the PreferenceScreen
 * If not everything wil be reset if the user clicks on other preferences on the screen.
 */
public class CustomTempRangePreference extends Preference{
    private static final int MIN_VALUE= -50;
    private static final int MAX_VALUE= 50;
    private static final String PREF_NAME = "prefValuesSaved";
    private static final String MIN_TEMP = "minTemp";
    private static final String MAX_TEMP= "maxTemp";
    private RangeSeekBar rsb;
    private SharedPreferences prefs;

    public CustomTempRangePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayoutResource(R.layout.temppref_layout);
    }
    /**
     * Creates the custom temperature range bar preference
     * sets the RangeSeekBar view component if it already have been initialized and there are
     * stored values in sharedPreferences.
     * @param parent
     * @return the created view component
     */
    @Override
    protected View onCreateView(ViewGroup parent) {
        View v = super.onCreateView(parent);
        rsb = (RangeSeekBar) v.findViewById(R.id.tempBarLayout);
        prefs = getContext().getSharedPreferences(PREF_NAME, getContext().MODE_PRIVATE);
        Log.i("TempSaved", "" + prefs.getInt("minTemp", MIN_VALUE) + ", " + prefs.getInt("maxTemp", MAX_VALUE));
        if(rsb != null ) {
            rsb.setSelectedMinValue(prefs.getInt(MIN_TEMP, MIN_VALUE));
            rsb.setSelectedMaxValue(prefs.getInt(MAX_TEMP, MAX_VALUE));
        }
        return v;
    }
    /**
     * Binds the view and an onChangeListener so if the preferences changes, new values wil be stored.
     * @param view the view of the Preference
     */
    @Override
    public void onBindView(final View view) {
        super.onBindView(view);
        rsb = (RangeSeekBar) view.findViewById(R.id.tempBarLayout);
        rsb.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                int min = rsb.getSelectedMinValue().intValue();
                int max = rsb.getSelectedMaxValue().intValue();
                SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(MIN_TEMP, min);
                editor.putInt(MAX_TEMP, max);
                editor.apply();
            }
        });
    }
}
