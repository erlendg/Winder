package com.eim.winder;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.eim.winder.activities.main.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.java.no.Gitt;
import cucumber.api.java.no.Når;
import cucumber.api.java.no.Så;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

/**
 * Created by Mari on 14.03.2016.
 */
@CucumberOptions(monochrome = true,plugin = {"html:target/cucumber-html-report", "json:target/cucumber-json-report.json" })
@LargeTest
public class SjekkStedslisteStepsDef {
    private String TAG = "SjekkStedslisteStepsDef";

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Gitt("^at appen er åpnet$")
    public void at_appen_er_åpnet() {
        Log.d(TAG, "Gitt at appen er åpen");
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



    @Når("^brukeren går inn på hovedsiden$")
    public void brukeren_går_inn_på_hovedsiden() {
        Log.d(TAG, "Når brukeren går inn på hovedsiden");
        onView(withId(R.id.activity_main));
    }


    @Så("^skal det framvises en oversikt over brukerens registrerte steder$")
    public void skal_det_framvises_en_oversikt_over_brukerens_registrerte_steder(){
        Log.d(TAG, "Så skal det framvises en oversikt over brukerens registrerte steder");
        String lableText =   mainActivity.getActivity().getResources().getString(R.string.check_interval);
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText(lableText))));
        onView(withId(R.id.fab));
    }
}
