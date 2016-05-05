package com.eim.winder;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.AlertSettingsRepo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;

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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

/**
 * Created by Mari on 10.04.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LeggTilNyttStedStepsDef1 {
    private ArrayList<AlertSettings> asd;
    private int size = 0;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    /**
     * Test requires that less then 10 alerts is registered in the list for the test to succeed.
     */

    @Test
    public void legg_til_nytt_sted_scenario1(){
        at_bruker_har_mindre_enn_ti_steder_registrert();
        brukeren_trykker_på_legg_til_nytt_sted_knappen();
        skal_forespørselen_aksepteres();
        brukeren_sendes_til_en_ny_stedsinnstillings_side();
    }

    @Gitt("^at bruker har mindre enn ti steder registrert$")
    public void at_bruker_har_mindre_enn_ti_steder_registrert(){
        // Express the Regexp above with the code you wish you had
        AlertSettingsRepo testService = Mockito.mock(AlertSettingsRepo.class);
        when(testService.getAllAlertSettings()).thenCallRealMethod();
        asd = mainActivity.getActivity().getRecycleViewDataset();
        size = asd.size();
        assertTrue(size < 10);
    }

    @Når("^brukeren trykker på legg-til-nytt-sted-knappen$")
    public void brukeren_trykker_på_legg_til_nytt_sted_knappen() {
        onView(withId(R.id.fab)).perform(click());
    }


    @Så("^skal forespørselen aksepteres$")
    public void skal_forespørselen_aksepteres() {
        //onView(withText(R.string.toast_more_then_ten_alerts)).inRoot(withDecorView(not(is(mainActivity.getActivity().getWindow().getDecorView())))).check(doesNotExist());
        onView(withText(R.string.toast_more_then_ten_alerts))
                .inRoot(withDecorView(not(mainActivity.getActivity().getWindow().getDecorView())))
                .check(doesNotExist());
    }

    @Og("^brukeren sendes til en ny stedsinnstillings-side$")
    public void brukeren_sendes_til_en_ny_stedsinnstillings_side(){
        onView(withId(R.id.search_view)).check(matches(isDisplayed()));
    }


}
