package com.eim.winder;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.eim.winder.R;
import com.eim.winder.RecyclerViewMatcher;
import com.eim.winder.activities.main.MainActivity;
import com.eim.winder.activities.selectlocation.SelectLocationActivity;
import com.eim.winder.db.DBService;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cucumber.api.PendingException;
import cucumber.api.java.no.Gitt;
import cucumber.api.java.no.Når;
import cucumber.api.java.no.Så;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Mari on 04.05.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MottaAutomatiskVarselStepsDef {
    /**
     * Test requires a 1min delay for the alert to occur after saving.
     * Also requires less then 4 locations in the weather alert list.
     * Difficult to test android system events, the view needs to be fully displayed, therefore
     * the Recycleview cannot have too many items so some of them doesn't get displayed
     */
    @Rule
    public ActivityTestRule mainActivity = new ActivityTestRule(MainActivity.class);

    private UiDevice mDevice;
    private int size;

    /**
     * This method is needed to setup a UiDevice so that the test can access the notification bar
     * to search for the alert.
     */

    @Before
    public void setUp(){
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    /**
     * Runner:
     */
    @Test
    public void motta_automatisk_varsel(){
        legg_til_sikre_inst_for_varsel(mainActivity);
        at_brukeren_har_registrert_et_sted_for_varsel();
        try {
            Thread.sleep(60000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //Checks green icon on last list item:
        de_registrerte_værinnstillingene_inntreffer(size);

        skal_brukeren_få_et_automatisk_varsel_fra_appen();
    }



    @Gitt("^at brukeren har registrert et sted for varsel$")
    public void at_brukeren_har_registrert_et_sted_for_varsel() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }

    @Når("^de registrerte værinnstillingene inntreffer$")
    public void de_registrerte_værinnstillingene_inntreffer(int pos)  {
        RecyclerViewMatcher rvmatcher = new RecyclerViewMatcher(R.id.recycler_view);
        onView(rvmatcher
                .atPositionInView(pos, R.id.weather_photo))
                .check(matches(isSelected()));
    }

    @Så("^skal brukeren få et automatisk varsel fra appen$")
    public void skal_brukeren_få_et_automatisk_varsel_fra_appen()  {
        mDevice.openNotification();

    }
    /**
     * Inserts an location that is most likely to get a instant match when forecasts is updated
     * @param mainActivity needs MainActivity to use methods:
     */
    private void legg_til_sikre_inst_for_varsel(ActivityTestRule mainActivity){
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        size = view.getAdapter().getItemCount();
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_view)).perform(typeText("Bergli, Grend, (Vestby, Akershus)"), closeSoftKeyboard());
        //need to set location manually because Espresso cannot handle clicks on AutoCompleteTextView's
        int loc_id = 326;
        SelectLocationActivity activity = SelectLocationActivity.getInstance();
        DBService db = new DBService(activity.getApplicationContext());
        activity.setLocation(db.getLocationFromId(loc_id));
        onView(withId(R.id.nextButton)).perform(click());
        onView(withText(R.string.preferences_temperature)).perform(click());
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(withListSize(size + 1)));
    }

    /**
     * Custom list size matcher
     * @param size
     * @return
     */
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((RecyclerView) view).getAdapter().getItemCount() == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("List should have " + size + " items");
            }
        };
    }
}
