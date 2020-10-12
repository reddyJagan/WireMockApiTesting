package com.example.wiremockapitesting

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InjectionFactory {
    var okHttpClient: OkHttpClient? = null

    /**
     * Build Retrofit 2
     */
    fun buildRetrofit(endpoint: String?): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(endpoint)
            .client(okHttpClient)
            .build()
    }

    init {
        val loggingInterceptor: Interceptor = HttpLoggingInterceptor()
            .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .build()
    }
}