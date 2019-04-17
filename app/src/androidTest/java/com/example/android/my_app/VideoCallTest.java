package com.example.android.my_app;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class VideoCallTest {

    @Rule
    public ActivityTestRule<VideoCall> mActivityTestRule = new ActivityTestRule<>(VideoCall.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.INTERNET",
                    "android.permission.CAMERA",
                    "android.permission.RECORD_AUDIO");

    @Test
    public void videoCallTest() {
    }
}
