package com.example.wiremockapitesting.tests.wiremock

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

class WireMockActivityInstrumentationTestCase2 {
    var logger = LoggerFactory.getLogger(
        WireMockActivityInstrumentationTestCase2::class.java
    )
    private var activity: MainActivity? = null

    @get:Rule
    var wireMockRule = WireMockRule(BuildConfig.PORT)

    @get:Rule
    var activityRule = ActivityTestRule(
        MainActivity::class.java
    )

    /**
     * Test WireMock
     */
    @Test
    fun testWiremock() {
        activity = activityRule.activity
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