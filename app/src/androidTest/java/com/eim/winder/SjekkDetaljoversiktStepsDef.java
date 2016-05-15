package com.eim.winder;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.eim.winder.activities.main.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cucumber.api.java.no.Gitt;
import cucumber.api.java.no.Når;
import cucumber.api.java.no.Og;
import cucumber.api.java.no.Så;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

/**
 * Created by Mari on 15.03.2016.

 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SjekkDetaljoversiktStepsDef {
    private String TAG = "SjekkDetaljoversiktStepsDef";
    /** The test requires that there are at least one item in the weaterlocations-list on the main page
    * To get access to the detailed view.
    */
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    /**
     * Runner:
     */
    @Test
    public void sjekk_detaljoversikt_scenario(){
        at_brukeren_har_åpnet_appen();
        har_registrerte_steder_i_listen();
        brukeren_trykker_på_stedet_for_detaljoversikt();
        skal_detaljoversikten_vises();
        brukeren_ser_alle_hendelser_for_stedet_i_oversikten();
    }


    @Gitt("^at brukeren har åpnet appen$")
    public void at_brukeren_har_åpnet_appen() {
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.app_name);
        matchToolbarTitle(title);
    }
    /**
     * Custom toolbar title view-matcher that searches trough the view for a toolbar with
     * a requested title
     * @param title name of toolbar
     * @return true if found
     */
    private static ViewInteraction matchToolbarTitle(CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class)).check(matches(hasToolbarTitle(is(title))));
    }
    private static Matcher<Object> hasToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("toolbar title: ");
                textMatcher.describeTo(description);
            }

        };
    }
    @Og("^har registrerte steder i listen$")
    public void har_registrerte_steder_i_listen() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        int size = view.getAdapter().getItemCount();
        assertTrue(size != 0);
    }


    @Når("^brukeren trykker på stedet for detaljoversikt$")
    public void brukeren_trykker_på_stedet_for_detaljoversikt(){
        // clicks on first element in the list:
       onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }


    @Så("^skal detaljoversikten vises$")
    public void skal_detaljoversikten_vises(){
        // checks for the location name in the toolbar and checks that the right layout xml-file is displayed
        String name = mainActivity.getActivity().getRecycleViewDataset().get(0).getLocation().getName();
        onView(withId(R.id.activity_alert_over_view)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar_layout)).check(matches(isDisplayed()));
    }


    @Og("^brukeren ser alle hendelser for stedet i oversikten$")
    public void brukeren_ser_alle_hendelser_for_stedet_i_oversikten() {
        //checks that the eventlist is displayed
        onView(withText(R.string.eventlist)).check(matches(isDisplayed()));
    }
}
