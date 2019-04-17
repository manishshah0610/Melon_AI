package com.example.android.my_app;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TutorialTest {

    @Rule
    public ActivityTestRule<Tutorial> mActivityTestRule = new ActivityTestRule<>(Tutorial.class);

    @Test
    public void tutorialTest() {
    }
}
