package com.example.ay31281_dev.myjenkinsapplication;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by ay31281-dev on 08/03/2018.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {


    /***********************************************************
     *  Rules
     **********************************************************/

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    /***********************************************************
     *  Test methods
     **********************************************************/

    @Test
    public void displaySnackbar() {

        onView(withId(R.id.fab)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Replace with your own action"))).check(matches(isDisplayed()));

    }
}
