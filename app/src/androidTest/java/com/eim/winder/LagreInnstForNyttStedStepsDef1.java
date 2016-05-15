package com.eim.winder;

import android.app.Activity;
import android.os.IBinder;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.activities.selectlocation.SelectLocationActivity;
import com.eim.winder.db.DBService;
import com.eim.winder.db.Location;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.InstanceOf;

import cucumber.api.PendingException;
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
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mari on 02.05.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LagreInnstForNyttStedStepsDef1 {
    /**
     * Test requires less then 10 saved locations in the weather alert list:
     */

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    private int beforeSize;

    /**
     * Runner:
     */
    @Test
    public void lagre_innstillinger_for_nytt_sted_scenario1(){
        at_bruker_har_valgt_et_nytt_sted();
        er_inne_på_innstillingssiden();
        brukeren_har_valgt_ønskede_innstillinger_for_stedet();
        trykker_på_lagre_knappen();
        skal_innstillingene_lagres();
        brukeren_blir_sendt_tilbake_til_hovedsiden();
    }

    /**
     * Forced to set the location manually because Espresso could not find the AutocompleteTextView and click on items inside it.
     */
    @Gitt("^at bruker har valgt et nytt sted$")
    public void at_bruker_har_valgt_et_nytt_sted(){
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        beforeSize = view.getAdapter().getItemCount();
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_view)).perform(typeText("Bergli, Grend, (Vestby, Akershus)"), closeSoftKeyboard());
        int loc_id = 326;
        SelectLocationActivity activity = SelectLocationActivity.getInstance();
        DBService db = new DBService(activity.getApplicationContext());
        activity.setLocation(db.getLocationFromId(loc_id));

        onView(withId(R.id.template_sun)).perform(click());
        onView(withId(R.id.template_wind)).perform(click());
        onView(withId(R.id.template_sun)).perform(click());
        onView(withId(R.id.nextButton)).perform(click());

    }

    @Og("^er inne på innstillingssiden$")
    public void er_inne_på_innstillingssiden() {
        matchToolbarTitle(mainActivity.getActivity().getApplicationContext().getString(R.string.settings_for_alert));
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

    @Når("^brukeren har valgt ønskede innstillinger for stedet$")
    public void brukeren_har_valgt_ønskede_innstillinger_for_stedet(){
        onView(withText(R.string.preferences_temperature)).perform(click());
    }

    @Og("^trykker på lagre-knappen$")
    public void trykker_på_lagre_knappen(){
        onView(withId(R.id.saveButton)).perform(click());
    }

    /**
     * Checks for increased list size:
     */
    @Så("^skal innstillingene lagres$")
    public void skal_innstillingene_lagres() {
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        int size = view.getAdapter().getItemCount();
        assertTrue(size == beforeSize+1);
    }


    @Og("^brukeren blir sendt tilbake til hovedsiden$")
    public void brukeren_blir_sendt_tilbake_til_hovedsiden(){
        String app_text = mainActivity.getActivity().getApplicationContext().getString(R.string.app_name);
        matchToolbarTitle(app_text);
    }
}
