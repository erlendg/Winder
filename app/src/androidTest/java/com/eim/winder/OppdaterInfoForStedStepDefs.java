package com.eim.winder;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.eim.winder.activities.main.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import cucumber.api.PendingException;
import cucumber.api.java.no.Gitt;
import cucumber.api.java.no.Når;
import cucumber.api.java.no.Og;
import cucumber.api.java.no.Så;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Mari on 04.05.2016.
 */
public class OppdaterInfoForStedStepDefs {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void oppdater_informasjon_for_sted_scenario1(){
        LagreInnstForNyttStedStepDefs1 temp = new LagreInnstForNyttStedStepDefs1();
        //temp.lagre_innstillinger_for_nytt_sted_scenario1();

        at_appen_er_åpnet();
        brukeren_er_inne_på_hovedsiden();
        brukeren_trykker_på_oppdater_knappen();
        skal_ny_værinformasjonen_framkomme(1);
    }
    @Test
    public void oppdater_informasjon_for_sted_scenario2(){
        LagreInnstForNyttStedStepDefs1 temp = new LagreInnstForNyttStedStepDefs1();
        //temp.lagre_innstillinger_for_nytt_sted_scenario1();

        at_appen_er_åpnet();
        brukeren_er_inne_på_hovedsiden();
        brukeren_trykker_på_oppdater_knappen();
        forekommer_ingen_endringer_hvis_nye_oppdateringer_ikke_er_tilgjengelig(0);
    }

    @Gitt("^at appen er åpnet$")
    public void at_appen_er_åpnet() {
        //String title = mainActivity.getActivity().getResources().getString(R.string.app_name);
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.app_name);
        matchToolbarTitle(title);
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
    @Og("^brukeren er inne på hovedsiden$")
    public void brukeren_er_inne_på_hovedsiden(){
        onView(withId(R.id.activity_main));
    }

    @Når("^brukeren trykker på oppdater-knappen$")
    public void brukeren_trykker_på_oppdater_knappen() {
        onView(withId(R.id.action_refresh)).perform(click());
    }

    @Så("^skal ny værinformasjonen framkomme$")
    public void skal_ny_værinformasjonen_framkomme(int pos) {
        RecyclerViewMatcher rvmatcher = new RecyclerViewMatcher(R.id.recycler_view);
        onView(rvmatcher
                .atPositionOnView(pos, R.id.weather_photo))
                .check(matches(isSelected()));
    }

    @Så("^forekommer ingen endringer hvis nye oppdateringer ikke er tilgjengelig$")
    public void forekommer_ingen_endringer_hvis_nye_oppdateringer_ikke_er_tilgjengelig(int pos){
        RecyclerViewMatcher rvmatcher = new RecyclerViewMatcher(R.id.recycler_view);
        onView(rvmatcher
                .atPositionOnView(0, R.id.weather_photo))
                .check(matches(not(isSelected())));
    }
}
