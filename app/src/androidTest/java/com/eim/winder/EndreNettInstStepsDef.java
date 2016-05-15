package com.eim.winder;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.eim.winder.R;
import com.eim.winder.activities.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cucumber.api.PendingException;
import cucumber.api.java.no.Gitt;
import cucumber.api.java.no.Når;
import cucumber.api.java.no.Så;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Mari on 04.05.2016.
 */
@RunWith(AndroidJUnit4.class)
public class EndreNettInstStepsDef {
    private SharedPreferences sp;
    boolean useMobileData;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);
    /**
     * Runner:
     */
    @Test
    public void endre_nettverksinnstillinger_scenario(){
        at_brukeren_er_inne_på_siden_med_appinnstillinger();
        brukeren_trykker_på_innstillingen_for_mobildata();
        skal_appen_endre_sine_nettverksinnstillinger_for_henting_av_værvarsel();
    }


    @Gitt("^at brukeren er inne på siden med appinnstillinger$")
    public void at_brukeren_er_inne_på_siden_med_appinnstillinger() {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withText(R.string.settings_header)).check(matches(isDisplayed()));
    }

    /**
     * Finds the selected value:
     */
    @Når("^brukeren trykker på innstillingen for mobildata$")
    public void brukeren_trykker_på_innstillingen_for_mobildata(){
        sp = PreferenceManager.getDefaultSharedPreferences(mainActivity.getActivity().getApplicationContext());
        useMobileData = sp.getBoolean("prefUseMobileData", true);
        onView(withText(R.string.pref_use_mobile_data)).perform(click());

    }

    /**
     * Checks that the network preferences is changed:
     */
    @Så("^skal appen endre sine nettverksinnstillinger for henting av værvarsel$")
    public void skal_appen_endre_sine_nettverksinnstillinger_for_henting_av_værvarsel(){
        sp = PreferenceManager.getDefaultSharedPreferences(mainActivity.getActivity().getApplicationContext());
        boolean useMobileData2 = sp.getBoolean("prefUseMobileData", true);
        assertTrue(useMobileData != useMobileData2);
        onView(withText(R.string.pref_use_mobile_data)).perform(click());
    }
}
