package com.eim.winder;

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
import android.view.View;

import com.eim.winder.activities.alertoverview.AlertOverViewActivity;
import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.db.AlertSettingsDAO;
import com.eim.winder.db.AlertSettingsRepo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@LargeTest
public class SettVarselStepsDef {
    private static final String TAG = "SlettVarselStepsDef";
    private String deletedLocation = "";
    private int size = 0;
    private int id = 0;
    private ArrayList<AlertSettingsDAO> asd;

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

    @Og("^har registrerte steder i listen$")
    public void har_registrerte_steder_i_listen() {
        at_brukeren_har_åpnet_appen();
        AlertSettingsRepo testService = Mockito.mock(AlertSettingsRepo.class);
        when(testService.getAllAlertSettings()).thenCallRealMethod();
        asd = mainActivity.getActivity().getAlertSettingsDataSet();
        size = asd.size();
        id = asd.get(0).getId();
        deletedLocation = asd.get(0).getLocation().getName();
        assertTrue(size != 0);
        onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(withListSize(size)));
    }

    /**
     * Custom Matcher for list size
     * @param size
     * @return matcher
     */
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View> () {
            @Override public boolean matchesSafely (final View view) {
                return ((RecyclerView) view).getChildCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText("RecycleView should have " + size + " items");
            }
        };
    }

    @Og("^har trykket på stedet for detaljoversikt$")
    public void har_trykket_på_stedet_for_detaljoversikt(){
        har_registrerte_steder_i_listen();
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    @Når("^brukeren trykker på slett-knappen$")
    public void brukeren_trykker_på_slett_knappen(){
        har_trykket_på_stedet_for_detaljoversikt();
        AlertSettingsRepo testService = Mockito.mock(AlertSettingsRepo.class);
        when(testService.deleteAlertSettings(id)).thenReturn(true);
        AlertOverViewActivity testActivity = Mockito.mock(AlertOverViewActivity.class);
        BDDMockito.willDoNothing().given(testActivity).cancelAlarm(id);
        onView(withId(R.id.fab_deleteItem)).perform(click());
        onView(withText("Do you want to delete the alert?")).check(matches(isDisplayed()));
        onView(withText(android.R.string.yes)).perform(click());
    }

    @Så("^skal stedet slettes$")
    public void skal_stedet_slettes() {
        brukeren_trykker_på_slett_knappen();
        MainActivity testActivity = Mockito.mock(MainActivity.class);
        asd.remove(0);
        when(testActivity.getAlertSettingsDataSet()).thenReturn(asd);
        assertTrue(mainActivity.getActivity().getAlertSettingsDataSet().size() == size - 1);
    }

    @Og("^brukeren returneres til hovedoversikten$")
    public void brukeren_returneres_til_hovedoversikten() {
        skal_stedet_slettes();
        at_brukeren_har_åpnet_appen();

    }

    @Og("^stedet er slettet fra listen med registrerte steder$")
    public void stedet_er_slettet_fra_listen_med_registrerte_steder()  {
        brukeren_returneres_til_hovedoversikten();
        onView(withText(deletedLocation)).check(doesNotExist());

    }
}
