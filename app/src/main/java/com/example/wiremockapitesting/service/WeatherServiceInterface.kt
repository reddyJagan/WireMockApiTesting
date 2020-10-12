package com.example.wiremockapitesting.service

import com.example.wiremockapitesting.BuildConfig
import com.example.wiremockapitesting.model.ConditionsServiceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherServiceInterface {
    @GET("/api/" + BuildConfig.WEATHERVIEW_API_KEY + "/conditions/q/CA/{location}.json")
    fun getConditions(@Path("location") location: String?): Call<ConditionsServiceResponse?>?

}