package com.eim.winder;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import com.eim.winder.activities.main.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cucumber.api.PendingException;
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
import static org.hamcrest.Matchers.is;

/**
 * Created by Mari on 04.05.2016.
 */
@RunWith(AndroidJUnit4.class)
public class EndreInnstForEtStedStepDefs {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void endre_innstillinger_for_et_sted_scenario(){
        at_brukeren_har_funnet_stedet_i_listen_over_registrerte_steder();
        har_trykket_på_stedet_for_detaljoversikt();
        brukeren_trykker_på_endre_knappen();
        skal_varsel_innstillingene_for_stedet_åpnes();
        brukeren_vil_kunne_endre_de_allerede_definerte_innstillingene_til_stedet();
    }

    @Gitt("^at brukeren har funnet stedet i listen over registrerte steder$")
    public void at_brukeren_har_funnet_stedet_i_listen_over_registrerte_steder() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }
    @Og("^har trykket på stedet for detaljoversikt$")
    public void har_trykket_på_stedet_for_detaljoversikt(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Når("^brukeren trykker på endre-knappen$")
    public void brukeren_trykker_på_endre_knappen(){
        onView(withId(R.id.fab_edit)).perform(click());
    }

    @Så("^skal varsel innstillingene for stedet åpnes$")
    public void skal_varsel_innstillingene_for_stedet_åpnes(){
        matchToolbarTitle(mainActivity.getActivity().getApplicationContext().getString(R.string.settings_for_alert));
    }
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

    @Og("^brukeren vil kunne endre de allerede definerte innstillingene til stedet$")
    public void brukeren_vil_kunne_endre_de_allerede_definerte_innstillingene_til_stedet() {
        onView(withText(R.string.preferences_temperature)).perform(click());
        onView(withText(R.string.preferences_precipitation)).perform(click());
        onView(withId(R.id.saveButton)).perform(click());
    }
}
