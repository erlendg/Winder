package com.eim.winder;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.eim.winder.activities.MainActivity;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsDSService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;

import cucumber.api.PendingException;
import cucumber.api.java.no.Gitt;
import cucumber.api.java.no.Når;
import cucumber.api.java.no.Og;
import cucumber.api.java.no.Så;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

/**
 * Created by Mari on 10.04.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LeggTilNyttStedStepDef2 {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    private ArrayList<AlertSettingsDAO> asd;
    private int size;

    @Test
    @Gitt("^at bruker har ti steder registrert$")
    public void at_bruker_har_ti_steder_registrert(){
        AlertSettingsDSService testService = Mockito.mock(AlertSettingsDSService.class);
        when(testService.getAllAlertSettings()).thenCallRealMethod();
        asd = mainActivity.getActivity().getAlertSettingsDataSet();
        size = asd.size();
        assertTrue(size == 10);


    }
    @Når("^brukeren trykker på legg-til-nytt-sted-knappen$")
    public void brukeren_trykker_på_legg_til_nytt_sted_knappen() {
        at_bruker_har_ti_steder_registrert();
        onView(withId(R.id.fab)).perform(click());
    }
    @Så("^skal forespørselen nektes$")
    public void skal_forespørselen_nektes(){
        onView(withId(R.id.search_view)).check(doesNotExist());
    }

    @Og("^brukeren skal få tilbakemelding om at han/hun allerede har maks antall steder registrert$")
    public void brukeren_skal_få_tilbakemelding_om_at_han_hun_allerede_har_maks_antall_steder_registrert() {
        onView(withText(R.string.toast_more_then_ten_alerts)).inRoot(withDecorView(not(mainActivity.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}