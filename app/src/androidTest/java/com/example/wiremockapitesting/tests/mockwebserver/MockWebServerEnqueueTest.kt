package com.example.wiremockapitesting.tests.mockwebserver

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.example.wiremockapitesting.BuildConfig
import com.example.wiremockapitesting.MainActivity
import com.example.wiremockapitesting.R
import com.example.wiremockapitesting.service.WeatherServiceManager
import com.example.wiremockapitesting.util.AssetReaderUtil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.slf4j.LoggerFactory

class MockWebServerEnqueueTest {
    var logger = LoggerFactory.getLogger(MockWebServerEnqueueTest::class.java)
    val mMockWebServer = MockWebServer()

    @get:Rule
    var activityRule = ActivityTestRule(
        MainActivity::class.java
    )
    private var activity: MainActivity? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        activity = activityRule.activity
        mMockWebServer.start(BuildConfig.PORT)
        //script MockWebServer to return this JSON
        val assetJson = AssetReaderUtil.asset(activity!!, "atlanta-conditions.json")
        mMockWebServer.enqueue(MockResponse().setBody(assetJson))
        val okhttpMockWebServerUrl = mMockWebServer.url("/").toString()
        logger.debug("okhttp mockserver URL: $okhttpMockWebServerUrl")
        val serviceEndpoint =
            "http://127.0.0.1:" + BuildConfig.PORT
        logger.debug("MockWebServer Endpoint: $serviceEndpoint")
        activity!!.setWeatherServiceManager(WeatherServiceManager(serviceEndpoint))
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mMockWebServer.shutdown()
    }

    /**
     * Test okhttp mockwebserver Enqueue
     */
    @Test
    fun testMockWebServerEnqueue() {
        activity = activityRule.activity
        logger.debug("testMockWebServerEnqueue")
        Espresso.onView(ViewMatchers.withId(R.id.editText))
            .perform(ViewActions.replaceText("atlanta"))
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textView))
            .check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("GA"))))
    }
}