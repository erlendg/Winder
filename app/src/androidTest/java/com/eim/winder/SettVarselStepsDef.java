package com.eim.winder;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;

import cucumber.api.PendingException;
import cucumber.api.java.no.Gitt;
import cucumber.api.java.no.Når;
import cucumber.api.java.no.Og;
import cucumber.api.java.no.Så;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

/**
 * Created by Mari on 17.03.2016.
 */
public class SettVarselStepsDef {
    private static final String TAG = "SlettVarselStepsDef";

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Gitt("^at brukeren har åpnet appen")
    public void at_brukeren_har_åpnet_appen(){
        Log.d(TAG, "Gitt at brukeren har åpnet appen");
        //String title = mainActivity.getActivity().getResources().getString(R.string.app_name);
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.app_name);
        matchToolbarTitle(title);
    }
    private static ViewInteraction matchToolbarTitle(CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class)).check(matches(hasToolbarTitle(is(title))));
    }
    private static Matcher<Object> hasToolbarTitle(final Matcher<CharSequence> textMatcher) {
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
    @Og("^har trykket på stedet for detaljoversikt$")
    public void har_trykket_på_stedet_for_detaljoversikt(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Når("^brukeren trykker på slett-knappen$")
    public void brukeren_trykker_på_slett_knappen(){

    }

    @Så("^skal stedet slettes fra listen med registrerte steder$")
    public void skal_stedet_slettes_fra_listen_med_registrerte_steder() {

    }
}
