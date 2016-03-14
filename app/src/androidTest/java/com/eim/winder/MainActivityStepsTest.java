package com.eim.winder;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.TextClock;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cucumber.api.PendingException;
import cucumber.api.java.no.Gitt;
import cucumber.api.java.no.Når;
import cucumber.api.java.no.Så;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Mari on 14.03.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityStepsTest {
    private String TAG = "MainActivityStepsTest";

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    @Gitt("^at appen er åpnet$")
    public void at_appen_er_åpnet(){
        Log.d(TAG, "Gitt at appen er åpen");
        String mystring = mainActivity.getActivity().getResources().getString(R.string.app_name);
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.app_name);
        onView(withId(R.id.toolbar).check(matches(title));

    }
    private static ViewInteraction matchToolbarTitle(CharSequence title) {
        return onView(allOf(isAssignableFrom(TextView.class))), withParent(isAssignableFrom(Toolbar.class))
                .check(matches(withText(title.toString())));
    }

    @Test
    @Når("^brukeren går inn på hovedsiden$")
    public void brukeren_går_inn_på_hovedsiden() {
        Log.d(TAG, "-----------------Gitt at appen er åpen");
        onView(withId(R.id.fab));

    }

    @Test
    @Så("^skal det framvises en oversikt over brukerens registrerte steder$")
    public void skal_det_framvises_en_oversikt_over_brukerens_registrerte_steder(){
        // Express the Regexp above with the code you wish you had
    }
}
