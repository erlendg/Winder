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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.activities.selectlocation.SelectLocationActivity;
import com.eim.winder.db.DBService;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
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
public class OppdaterInfoForStedStepDef {
    private int size;


    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    /**
     *
     * Tests requires that at least one element is present in the list,
     * cannot click on new-button when it is animated on empty list
     */

    @Test
    public void oppdater_informasjon_for_sted_scenario1()throws InterruptedException{
        legg_til_sikre_inst_for_varsel(mainActivity);

        at_appen_er_åpnet();
        brukeren_er_inne_på_hovedsiden();
        brukeren_trykker_på_oppdater_knappen();
        Thread.sleep(1000);
        skal_ny_værinformasjonen_framkomme(size);
    }
    @Test
    public void oppdater_informasjon_for_sted_scenario2(){
        legg_til_usikre_inst_for_varsel(mainActivity);

        at_appen_er_åpnet();
        brukeren_er_inne_på_hovedsiden();
        brukeren_trykker_på_oppdater_knappen();
        forekommer_ingen_endringer_hvis_nye_oppdateringer_ikke_er_tilgjengelig(size);
    }
    private void legg_til_sikre_inst_for_varsel(ActivityTestRule mainActivity) {
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        size = view.getChildCount();
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_view)).perform(typeText("Bergli, Grend, (Vestby, Akershus)"), closeSoftKeyboard());
        int loc_id = 326;
        SelectLocationActivity activity = SelectLocationActivity.getInstance();
        DBService db = new DBService(activity.getApplicationContext());
        activity.setLocation(db.getLocationFromId(loc_id));
        onView(withId(R.id.nextButton)).perform(click());
        onView(withText(R.string.preferences_temperature)).perform(click());
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(withListSize(size + 1)));
    }
    private void legg_til_usikre_inst_for_varsel(ActivityTestRule mainActivity) {
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        size = view.getChildCount();
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_view)).perform(typeText("Bergli, Grend, (Vestby, Akershus)"), closeSoftKeyboard());
        int loc_id = 326;
        SelectLocationActivity activity = SelectLocationActivity.getInstance();
        DBService db = new DBService(activity.getApplicationContext());
        activity.setLocation(db.getLocationFromId(loc_id));
        onView(withId(R.id.template_sun)).perform(click());
        onView(withId(R.id.nextButton)).perform(click());
        onView(withText(R.string.preferences_precipitation)).perform(click());
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(withListSize(size + 1)));
    }
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((RecyclerView) view).getChildCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
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
                .atPositionOnView(pos, R.id.weather_photo))
                .check(matches(not(isSelected())));
    }
}
