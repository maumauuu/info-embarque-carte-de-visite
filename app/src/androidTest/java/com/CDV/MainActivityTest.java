package com.CDV;


import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;


import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {



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
    //    fragment.getView().findViewById();
      //  fragment.getView().findViewById(Profil);

    }



}
