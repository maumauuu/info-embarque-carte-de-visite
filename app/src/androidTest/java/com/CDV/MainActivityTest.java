package com.CDV;


import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import android.support.test.runner.AndroidJUnit4;

import com.CDV.fragment.SeeContactFragment;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.assertion.ViewAssertions.matches;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private SeeContactFragment seeContact;

    public MainActivityTest() {
        super(MainActivity.class);
    }


    @Before
    public void setup()  throws Exception {
        super.setUp();
       injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    @Test
    public void testInitial() {
        onView(withId(R.id.layoutcdv))
                .check(matches(isDisplayed()));
        onView(withId(R.id.layoutgenerer))
                .check(matches(isDisplayed()));
        onView(withId(R.id.layoutSend))
                .check(matches(isDisplayed()));


    }

    @Test
    public void testClickLayoutcdv() {
        onView(withId(R.id.layoutcdv))
                .check(matches(isDisplayed()));
        onView(withId(R.id.layoutcdv))
                .perform(click());
         onView(withId(R.id.cdv))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testClickLayoutsend() {
        onView(withId(R.id.layoutSend))
                .check(matches(isDisplayed()));
        onView(withId(R.id.layoutSend))
                .perform(click());
        onView(withId(R.id.Send))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testClickLayoutgenerer() {
        onView(withId(R.id.layoutgenerer))
                .check(matches(isDisplayed()));
        onView(withId(R.id.layoutgenerer))
                .perform(click());
        onView(withId(R.id.qr_code))
                .check(matches(isDisplayed()));

    }



}
