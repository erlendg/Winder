package com.eim.winder;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.eim.winder.activities.main.MainActivity;

import org.junit.Before;
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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Mari on 05.05.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EndreSpraakInstStepsDef {
    private SharedPreferences sp;
    private String selectedLanguage;
    private String defaultValue = "default";

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    /**
     * Runner:
     */

    @Test
    public void endre_språk_innstillinger_scenario(){
        at_brukeren_er_inne_på_siden_med_appinnstillinger();
        brukeren_trykker_på_innstillingen_for_språk();
        skal_brukeren_få_opp_en_liste_med_mulige_språk_for_appen();
        brukeren_kan_velge_ønsket_språk_fra_listen();
    }


    @Gitt("^at brukeren er inne på siden med appinnstillinger$")
    public void at_brukeren_er_inne_på_siden_med_appinnstillinger() {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withText(R.string.settings_header)).check(matches(isDisplayed()));
    }

    @Når("^brukeren trykker på innstillingen for språk$")
    public void brukeren_trykker_på_innstillingen_for_språk(){
        onView(withText(R.string.language_header)).perform(click());
    }

    @Så("^skal brukeren få opp en liste med mulige språk for appen$")
    public void skal_brukeren_få_opp_en_liste_med_mulige_språk_for_appen() {
        onView(withText(R.string.language_default)).check(matches(isDisplayed()));
        onView(withText(R.string.language_default)).perform(click());
        //Finds the selected language:
        sp = PreferenceManager.getDefaultSharedPreferences(mainActivity.getActivity().getApplicationContext());
        selectedLanguage = sp.getString(mainActivity.getActivity().getResources().getString(R.string.language_pref_key), defaultValue);
    }

    /**
     * Checks that the language is changed:
     */
    @Og("^brukeren kan velge ønsket språk fra listen$")
    public void brukeren_kan_velge_ønsket_språk_fra_listen(){
        onView(withText(R.string.language_header)).perform(click());
        onView(withText(R.string.language_norwegian)).perform(click());
        onView(withText(R.string.language_header)).perform(click());
        onView(withText(R.string.language_english)).perform(click());
        sp = PreferenceManager.getDefaultSharedPreferences(mainActivity.getActivity().getApplicationContext());
        String selectedLanguage2 = sp.getString(mainActivity.getActivity().getResources().getString(R.string.language_pref_key), defaultValue);
        assertTrue(selectedLanguage != selectedLanguage2);
    }
}
