package com.eim.winder;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;

import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.activities.selectlocation.SelectLocationActivity;
import com.eim.winder.db.DBService;

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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Mari on 03.05.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LagreInnstForNyttStedStepsDef2 {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void lagre_innstillinger_for_nytt_sted_scenario2(){
        at_bruker_har_valgt_et_nytt_sted();
        er_inne_på_innstillingssiden();
        ikke_har_valgt_gyldige_innstillinger();
        brukeren_trykker_på_lagre_knappen();
        skal_forespørselen_nektes();
        brukeren_får_en_feilmelding();
    }

    @Gitt("^at bruker har valgt et nytt sted$")
    public void at_bruker_har_valgt_et_nytt_sted(){
        //onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_view)).perform(typeText("Bergli, Grend, (Vestby, Akershus)"), closeSoftKeyboard());
        int loc_id = 326;
        SelectLocationActivity activity = SelectLocationActivity.getInstance();
        DBService db = new DBService(activity.getApplicationContext());
        activity.setLocation(db.getLocationFromId(loc_id));

        onView(withId(R.id.nextButton)).perform(click());

    }

    @Og("^er inne på innstillingssiden$")
    public void er_inne_på_innstillingssiden() {
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
    @Og("^ikke har valgt gyldige innstillinger$")
    public void ikke_har_valgt_gyldige_innstillinger() {
        onView(withText(R.string.preferences_winddir)).perform(click());
        onView(withText(R.string.preferences_winddir)).perform(click());
    }

    @Når("^brukeren trykker på lagre-knappen$")
    public void brukeren_trykker_på_lagre_knappen(){
        onView(withId(R.id.saveButton)).perform(click());
    }


    @Så("^skal forespørselen nektes$")
    public void skal_forespørselen_nektes(){
        matchToolbarTitle(mainActivity.getActivity().getApplicationContext().getString(R.string.settings_for_alert));
    }

    @Og("^brukeren får en feilmelding$")
    public void brukeren_får_en_feilmelding() {
        onView(allOf(withId(android.support.design.R.id.snackbar_text)))
                .check(matches(isDisplayed()));
    }
}
