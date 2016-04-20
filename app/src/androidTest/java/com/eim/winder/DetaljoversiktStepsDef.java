package com.eim.winder;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

/**
 * Created by Mari on 15.03.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetaljoversiktStepsDef {
    private String TAG = "DetaljoversiktStepsDef";

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    @Gitt("^at brukeren har åpnet appen$")
    public void at_brukeren_har_åpnet_appen() {
        Log.e(TAG, "Gitt at brukeren har åpnet appen");
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

    @Test
    @Når("^brukeren trykker på stedet for detaljoversikt$")
    public void brukeren_trykker_på_stedet_for_detaljoversikt(){
        // clicks on first element in the list:
        at_brukeren_har_åpnet_appen();
        Log.e(TAG, "Når brukeren trykker på stedet for detaljoversikt");
       onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    @Så("^skal detaljoversikten vises$")
    public void skal_detaljoversikten_vises(){
        brukeren_trykker_på_stedet_for_detaljoversikt();
        Log.e(TAG, "Så skal detaljoversikten vises");
        String name = mainActivity.getActivity().getAlertSettingsDataSet().get(0).getLocation().getName();
        onView(withId(R.id.activity_alert_over_view));
        onView(withId(R.id.toolbar_layout));
        matchCollapsingToolbarTitle(name);
    }
    private static ViewInteraction matchCollapsingToolbarTitle(CharSequence title) {
        return onView(isAssignableFrom(CollapsingToolbarLayout.class)).check(matches(hasCollapsingToolbarTitle(is(title))));
    }
    private static Matcher<Object> hasCollapsingToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, CollapsingToolbarLayout>(CollapsingToolbarLayout.class) {
            @Override public boolean matchesSafely(CollapsingToolbarLayout toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("has toolbar title: ");
                textMatcher.describeTo(description);
            }

        };
    }

    @Test
    @Og("^brukeren ser alle hendelser for stedet i oversikten$")
    public void brukeren_ser_alle_hendelser_for_stedet_i_oversikten() {
        skal_detaljoversikten_vises();
        Log.e(TAG, " Og brukeren ser alle hendelser for stedet i oversikten");
        String name = mainActivity.getActivity().getAlertSettingsDataSet().get(0).getLocation().toString();
        onView(withId(R.id.preferences_table));
        onView(withId(R.id.row1_title)).check(matches(withText(name)));

    }
}
