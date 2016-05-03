package com.eim.winder;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.activities.selectlocation.SelectLocationActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
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

/**
 * Created by Mari on 02.05.2016.
 */
@RunWith(AndroidJUnit4.class)
public class LagreInnstForNyttStedStepDefs1 {
    @Rule
    public ActivityTestRule<SelectLocationActivity> mainActivity = new ActivityTestRule<SelectLocationActivity>(SelectLocationActivity.class);

    @Test
    @Gitt("^at bruker har valgt et nytt sted$")
    public void at_bruker_har_valgt_et_nytt_sted(){
        //onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_view)).perform(typeText("Trondheim,"), closeSoftKeyboard());
        //onData(withText("Trondheim, By, (Trondheim, Sør-Trøndelag)")).perform(click());
       // onData(allOf(is(instanceOf(String.class)), is("Trondheim, By, (Trondheim, Sør-Trøndelag)"))).perform(click());
        /*onData(anything())
                .inAdapterView(isDescendantOfA(withId(R.id.search_view)))
                .atPosition(1)
                .perform(click());*/
        //onData(hasToString(startsWith("Trondheim"))).perform(click());
        /*onData(hasToString(startsWith("Trondheim,")))
                .inAdapterView(withId(R.id.search_view))
                .perform(click());


        onData(allOf(instanceOf(String.class), is("Trondheim, By, (Trondheim, Sør-Trøndelag)")))
                .inRoot(withDecorView(not(is(mainActivity.getActivity().getWindow().getDecorView()))))
                .perform(click());
        onView(withText("Trondheim, By, (Trondheim, Sør-Trøndelag)"))
                .inRoot(withDecorView(not(is(mainActivity.getActivity().getWindow().getDecorView()))))
                .perform(click());
        onData(anything())
                .inAdapterView(withClassName(equalTo(
                        "android.widget.ListPopupWindow$DropDownListView")))
                .atPosition(1)
                .perform(click());*/
        onView(allOf(withId(android.R.layout.simple_dropdown_item_1line), withText("Trondheim, By, (Trondheim, Sør-Trøndelag)"))).perform(click());
    }

    @Og("^er inne på innstillingssiden$")
    public void er_inne_på_innstillingssiden() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Når("^brukeren har valgt ønskede innstillinger for stedet$")
    public void brukeren_har_valgt_ønskede_innstillinger_for_stedet() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Og("^trykker på lagre-knappen$")
    public void trykker_på_lagre_knappen() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Så("^skal innstillingene lagres$")
    public void skal_innstillingene_lagres() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Og("^brukeren blir sendt tilbake til hovedsiden$")
    public void brukeren_blir_sendt_tilbake_til_hovedsiden() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }
}
