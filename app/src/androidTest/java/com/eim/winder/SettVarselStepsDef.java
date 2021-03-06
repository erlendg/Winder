package com.eim.winder;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.ActivityTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import com.eim.winder.activities.alertoverview.AlertOverViewActivity;
import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.db.AlertSettings;
import com.eim.winder.db.AlertSettingsRepo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.BDDMockito;
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
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkArgument;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.when;

/**
 * Created by Mari on 17.03.2016.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class SettVarselStepsDef extends ActivityTestCase {
    private static final String TAG = "SlettVarselStepsDef";
    private String deletedLocation = "";
    private int size = 0;
    private ArrayList<AlertSettings> asd;
    /**
     * Test requires the alert list to contain at least one item, with unique name
     * for Espresson to finds this item and delete it.
     * Be aware that this test vil delete the location if it succeeds.
     */

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    /**
     * Runner:
     */
    @Test
    public void slett_varsel_scenario(){
        at_brukeren_har_åpnet_appen();
        har_registrerte_steder_i_listen();
        har_trykket_på_stedet_for_detaljoversikt();
        brukeren_trykker_på_slett_knappen();
        brukeren_returneres_til_hovedoversikten();
        stedet_er_slettet_fra_listen_med_registrerte_steder();
    }

    @Gitt("^at brukeren har åpnet appen$")
    public void at_brukeren_har_åpnet_appen(){
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.app_name);
        matchToolbarTitle(title);
    }
    /**
     * Custom toolbar title view-matcher that searches trough the view for a toolbar with
     * a requested title
     * @param title name of toolbar
     * @return true if found
     */
    private static ViewInteraction matchToolbarTitle(
            CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class))
                .check(matches(hasToolbarTitle(is(title))));
    }
    private static Matcher<Object> hasToolbarTitle(
            final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("has toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    @Og("^har registrerte steder i listen$")
    public void har_registrerte_steder_i_listen() {
        //finds the recycleview and checks that it has items or children
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        size = view.getAdapter().getItemCount();
        assertTrue(size != 0);


        asd = mainActivity.getActivity().getRecycleViewDataset();
        deletedLocation = asd.get(0).getLocation().getName();

        onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(withListSize(size)));
    }

    /**
     * Custom Matcher for list size
     * @param size size of the list to be matched
     * @return matcher
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

    @Og("^har trykket på stedet for detaljoversikt$")
    public void har_trykket_på_stedet_for_detaljoversikt(){
        //Clicks on the first item in the list
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Når("^brukeren trykker på slett-knappen$")
    public void brukeren_trykker_på_slett_knappen(){
        //Clicks
        onView(withId(R.id.fab_deleteItem)).perform(click());
        //Checks for the confirmation button
        Context context = mainActivity.getActivity().getApplicationContext();
        String deleteMessage = context.getApplicationContext().getString(R.string.delete_message);
        onView(withText(deleteMessage)).check(matches(isDisplayed()));
        onView(withText(android.R.string.yes)).perform(click());
    }

    /**
     * Checks that the size ig the list is reduced by one.
     */
    @Så("^skal stedet slettes$")
    public void skal_stedet_slettes() {
        Log.v(TAG, "size:" + size);
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        int newsize = view.getAdapter().getItemCount();
        assertTrue(size == newsize - 1);
    }

    @Og("^brukeren returneres til hovedoversikten$")
    public void brukeren_returneres_til_hovedoversikten() {
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.app_name);
        matchToolbarTitle(title);

    }
    /**
     * Checks for the name in the view, this is why the name of the location to be deletet needs to be unique in the list.
     */
    @Og("^stedet er slettet fra listen med registrerte steder$")
    public void stedet_er_slettet_fra_listen_med_registrerte_steder()  {
        onView(withText(deletedLocation)).check(doesNotExist());
    }
}
