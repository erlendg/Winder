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
public class MottaAutomatiskVarselStepDef {

    @Rule
    public ActivityTestRule mainActivity = new ActivityTestRule(MainActivity.class);

    private UiDevice mDevice;


    @Before
    public void setUp(){
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void motta_automatisk_varsel(){
        legg_til_sikre_inst_for_varsel(mainActivity);
        at_brukeren_har_registrert_et_sted_for_varsel();
        try {
            Thread.sleep(60000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        de_registrerte_værinnstillingene_inntreffer(1);

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
                .atPositionOnView(pos, R.id.weather_photo))
                .check(matches(isSelected()));
    }

    @Så("^skal brukeren få et automatisk varsel fra appen$")
    public void skal_brukeren_få_et_automatisk_varsel_fra_appen()  {
        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.pkg("com.eim.winder")), 1000);

        UiSelector notificationStackScroller = new UiSelector().packageName("com.android.systemui")
                                                              .className("android.view.ViewGroup")
                                                              .resourceId(
                                                                      "com.android.systemui:id/notification_stack_scroller");
        UiObject notificationStackScrollerUiObject = mDevice.findObject(notificationStackScroller);
        assertTrue(notificationStackScrollerUiObject.exists());

        /*
         * access top notification in the center through parent
         */
        UiObject notiSelectorUiObject = null;
        try {
            notiSelectorUiObject = notificationStackScrollerUiObject.getChild(new UiSelector().index(0));
            notiSelectorUiObject.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(notiSelectorUiObject.exists());


    }
    private void legg_til_sikre_inst_for_varsel(ActivityTestRule mainActivity){
        RecyclerView view = (RecyclerView) mainActivity.getActivity().findViewById(R.id.recycler_view);
        int size = view.getChildCount();
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


        //onView(withId(R.id.action_refresh)).perform(click());

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
}
