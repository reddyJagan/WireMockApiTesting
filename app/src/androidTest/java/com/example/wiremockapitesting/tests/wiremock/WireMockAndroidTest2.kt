package com.example.wiremockapitesting.tests.wiremock

import android.content.Intent
import androidx.test.InstrumentationRegistry
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
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.slf4j.LoggerFactory

class WireMockAndroidTest2 {
    var logger = LoggerFactory.getLogger(WireMockAndroidTest2::class.java)
    private var activity: MainActivity? = null

    /* @Rule
     var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule<Any?>(
         MainActivity::class.java,
         true,  // initialTouchMode
         false
     ) // launchActivity. False to set intent per method*/
    @get:Rule
    var activityRule = ActivityTestRule(
        MainActivity::class.java
    )

    @get:Rule
    var wireMockRule = WireMockRule(BuildConfig.PORT)

    /**
     * Test WireMock
     */
    @Test
    fun testWiremock() {
        val applicationContext =
            InstrumentationRegistry.getTargetContext().applicationContext
        activity = activityRule.launchActivity(Intent(applicationContext, MainActivity::class.java))
        val jsonBody = AssetReaderUtil.asset(activity!!, "atlanta-conditions.json")
        WireMock.stubFor(
            WireMock.get(WireMock.urlMatching("/api/.*"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBody(jsonBody)
                )
        )
        val serviceEndpoint =
            "http://127.0.0.1:" + BuildConfig.PORT
        logger.debug("WireMock Endpoint: $serviceEndpoint")
        activity!!.setWeatherServiceManager(WeatherServiceManager(serviceEndpoint))
        Espresso.onView(ViewMatchers.withId(R.id.editText))
            .perform(ViewActions.replaceText("atlanta"))
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textView))
            .check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("GA"))))
    }
}