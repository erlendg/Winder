package com.eim.winder;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.activities.selectlocation.SelectLocationActivity;
import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.AlertSettingsRepo;
import com.eim.winder.db.DBService;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;

import cucumber.api.java.no.Gitt;
import cucumber.api.java.no.Når;
import cucumber.api.java.no.Og;
import cucumber.api.java.no.Så;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

/**
 * Created by Mari on 10.04.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LeggTilNyttStedStepsDef2 {
    /**
     * Test requires that 10 alerts is registered in the list for the test to succeed.
     */
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    private int size;

    @Test
    public void legg_til_nytt_sted_scenario2(){
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        size = view.getAdapter().getItemCount();
        while ( size!=10) {
            Log.d("HEIHEI", "WHILE");
            legg_til_usikre_inst_for_varsel(mainActivity);
            size = view.getAdapter().getItemCount();
        }
        at_bruker_har_ti_steder_registrert();
        brukeren_trykker_på_legg_til_nytt_sted_knappen();
        skal_forespørselen_nektes();
        brukeren_skal_få_tilbakemelding_om_at_han_hun_allerede_har_maks_antall_steder_registrert();
    }
    /**
     * Inserts an location that is not likely to get a instant match when forecasts is updated
     * @param mainActivity needs MainActivity to use methods:
     */
    private void legg_til_usikre_inst_for_varsel(ActivityTestRule mainActivity) {
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        int size = view.getAdapter().getItemCount();
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_view)).perform(typeText("Bergli, Grend, (Vestby, Akershus)"), closeSoftKeyboard());
        int loc_id = 326;
        //Forced to set the location manually because Espresso could not find the AutocompleteTextView and click on items inside it.
        SelectLocationActivity activity = SelectLocationActivity.getInstance();
        DBService db = new DBService(activity.getApplicationContext());
        activity.setLocation(db.getLocationFromId(loc_id));
        onView(withId(R.id.template_sun)).perform(click());
        onView(withId(R.id.nextButton)).perform(click());
        onView(withText(R.string.preferences_precipitation)).perform(click());
        onView(withId(R.id.saveButton)).perform(click());
        int afterSize = size+1;
        onView(withId(R.id.recycler_view)).check(matches(withListSize(afterSize)));
    }
    /**
     * Custom list size matcher
     * @param size of the list
     * @return true if the list size is equal to the given number.
     */
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((RecyclerView) view).getAdapter().getItemCount() == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("List should have " + size + " items");
            }
        };
    }

    /**
     * Sjekker at RecycleView (listen) inneholder 10 steder:
     */
    @Gitt("^at bruker har ti steder registrert$")
    public void at_bruker_har_ti_steder_registrert(){
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        size = view.getAdapter().getItemCount();
        assertTrue(size == 10);
    }

    @Når("^brukeren trykker på legg-til-nytt-sted-knappen$")
    public void brukeren_trykker_på_legg_til_nytt_sted_knappen() {
        onView(withId(R.id.fab)).perform(click());
    }

    @Så("^skal forespørselen nektes$")
    public void skal_forespørselen_nektes(){
        onView(withText(R.string.choose_location)).check(doesNotExist());
    }

    @Og("^brukeren skal få tilbakemelding om at han/hun allerede har maks antall steder registrert$")
    public void brukeren_skal_få_tilbakemelding_om_at_han_hun_allerede_har_maks_antall_steder_registrert() {
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.toast_more_then_ten_alerts)))
                .check(matches(isDisplayed()));
    }
}
