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
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.slf4j.LoggerFactory

class MockWebServerDispatcherTest {
    var logger = LoggerFactory.getLogger(MockWebServerDispatcherTest::class.java)
    val mMockWebServer = MockWebServer()
    private var activity: MainActivity? = null

    @get:Rule
    var activityRule = ActivityTestRule(
        MainActivity::class.java
    )

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mMockWebServer.start(BuildConfig.PORT)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mMockWebServer.shutdown()
    }

    /**
     * Test okhttp mockwebserver Dispatcher
     */
    @Test
    fun testMockWebServerDispatcher() {
        activity = activityRule.activity
        logger.debug("testMockWebServerDispatcher")

        //Use a dispatcher
        val dispatcher: Dispatcher =
            object : Dispatcher() {
                @Throws(InterruptedException::class)
                override fun dispatch(request: RecordedRequest): MockResponse {
                    if (request.path == "/api/840dbdf2737a7ff9/conditions/q/CA/atlanta.json") {
                        val jsonBody =
                            AssetReaderUtil.asset(activity!!, "atlanta-conditions.json")
                        return MockResponse().setResponseCode(200).setBody(jsonBody)
                    }
                    return MockResponse().setResponseCode(404)
                }
            }
        mMockWebServer.dispatcher = dispatcher
        val okhttpMockWebServerUrl = mMockWebServer.url("/").toString()
        logger.debug("okhttp mockserver URL: $okhttpMockWebServerUrl")
        activity!!.setWeatherServiceManager(WeatherServiceManager(okhttpMockWebServerUrl))

        //   Spoon.screenshot(activity, "one");
        Espresso.onView(ViewMatchers.withId(R.id.editText))
            .perform(ViewActions.replaceText("atlanta"))
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click())
        //   Spoon.screenshot(activity, "two");
        Espresso.onView(ViewMatchers.withId(R.id.textView))
            .check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("GA"))))
    }
}