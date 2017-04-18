package com.CDV;


import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.CDV.fragment.AddContactFragment;
import com.CDV.fragment.GestionContactFragment;
import com.CDV.fragment.Profil;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private AddContactFragment fragment;


    public MainActivityTest() {
        super(MainActivity.class);
    }


    @Before
    public void setup()  throws Exception {
        super.setUp();
        fragment = new AddContactFragment();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    @Test
    public void testInitial() {
    //    fragment.getView().findViewById();
      //  fragment.getView().findViewById(Profil);

    }



}
