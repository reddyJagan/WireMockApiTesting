package com.example.wiremockapitesting.tests.wiremock

import android.content.Context
import androidx.test.InstrumentationRegistry
import com.example.wiremockapitesting.BuildConfig
import com.example.wiremockapitesting.util.AssetReaderUtil
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.IOException

class WireMockApplicationTestCase {
    var logger = LoggerFactory.getLogger(WireMockApplicationTestCase::class.java)
    private var applicationContext: Context? = null

    /**
     * The @Rule - WireMockRule does NOT currently work for the ApplicationTestCase because it is not based on JUnit3 and not JUnit4 so we need to create & manage the WireMockServer ourselves
     *
     *
     * As of 09.09.2015 - "To test an Android application object on the Android runtime you use the ApplicationTestCase class.
     * It is expected that Google will soon provide a special JUnit4 rule for testing the application object but at the moment his is not yet available."
     *
     *
     * Reference: http://www.vogella.com/tutorials/AndroidTesting/article.html
     */
    var wireMockServer =
        WireMockServer(BuildConfig.PORT)

    @Before
    fun setUp() {
        applicationContext =
            InstrumentationRegistry.getTargetContext().applicationContext
        wireMockServer.start()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        wireMockServer.stop()
    }

    /**
     * Test WireMock, but just the Http Call.  Make sure the response matches the mock we want.
     */
    @Test
    @Throws(IOException::class)
    fun testWiremockPlusOkHttp() {
        logger.debug("testWiremockPlusOkHttp")
        val uri = "/api/840dbdf2737a7ff9/conditions/q/CA/atlanta.json"
        val jsonBody =
            AssetReaderUtil.asset(applicationContext!!, "atlanta-conditions.json")
        Assert.assertFalse(jsonBody.isEmpty())
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlMatching(uri))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBody(jsonBody)
                )
        )
        val serviceEndpoint =
            "http://127.0.0.1:" + BuildConfig.PORT
        logger.debug("WireMock Endpoint: $serviceEndpoint")
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(serviceEndpoint + uri)
            .build()
        val response = okHttpClient.newCall(request).execute()
        Assert.assertEquals(jsonBody, response.body()!!.string())
    }
}