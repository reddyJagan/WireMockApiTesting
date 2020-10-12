package com.example.wiremockapitesting.tests

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.wiremockapitesting.MainActivity
import com.example.wiremockapitesting.R
import com.example.wiremockapitesting.InjectionFactory
import com.jakewharton.espresso.OkHttp3IdlingResource
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory

@RunWith(AndroidJUnit4::class)
class RealServerTest {
    var logger = LoggerFactory.getLogger(RealServerTest::class.java)

    @get:Rule
    var activityRule = ActivityTestRule(
        MainActivity::class.java
    )
    var idlingResource: IdlingResource? = null

    @Before
    fun setUp() {
        idlingResource = OkHttp3IdlingResource.create("OkHttp", InjectionFactory.okHttpClient!!)
        Espresso.registerIdlingResources(idlingResource)
    }

    @After
    fun tearDown() {
        Espresso.unregisterIdlingResources(idlingResource)
    }

    /**
     * Test Real API
     */
    @Test
    fun testRealAPI() {
        logger.debug("testRealAPI")
        Espresso.onView(ViewMatchers.withId(R.id.editText))
            .perform(ViewActions.replaceText("atlanta, ga"))
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textView))
            .check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("GA"))))
    }
}