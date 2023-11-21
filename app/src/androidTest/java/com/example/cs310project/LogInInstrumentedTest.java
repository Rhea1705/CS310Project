package com.example.cs310project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.StringContains.containsString;

import static org.hamcrest.Matchers.not;

import android.app.Activity;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.rule.IntentsRule;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LogInInstrumentedTest {

    @Rule
    public ActivityScenarioRule<LogIn> mIntentrule = new ActivityScenarioRule<>(LogIn.class);

    @Before
    public void setUp() {
        Intents.init(); // Initialize Intents before the test
    }
    @Test
    public void testloginflowvalid(){
        //Espresso.onView(ViewMatchers.withId(R.id.logInNav)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.ID)).perform(ViewActions.typeText("viditjuneja@outlook.com"));
        onView(ViewMatchers.withId(R.id.Password)).perform(ViewActions.typeText("Junoo1902"));

        onView(ViewMatchers.withId(R.id.idBtnRegister)).perform(ViewActions.click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intents.intended(IntentMatchers.hasComponent(DepartmentsActivity.class.getName()));



    }


}

