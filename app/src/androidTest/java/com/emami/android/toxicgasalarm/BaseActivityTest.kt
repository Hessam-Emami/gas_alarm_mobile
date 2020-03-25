package com.emami.android.toxicgasalarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.emami.android.toxicgasalarm.ui.MainActivity
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaseActivityTest {

    @get:Rule
    var activityTestRule = ActivityTestRule<MainActivity>(
        MainActivity::class.java)


}