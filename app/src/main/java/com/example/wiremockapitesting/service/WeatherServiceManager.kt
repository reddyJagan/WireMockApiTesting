package com.example.wiremockapitesting.service


import com.example.wiremockapitesting.InjectionFactory.buildRetrofit
import com.example.wiremockapitesting.model.ConditionsServiceResponse
import retrofit2.Call

class WeatherServiceManager(weatherServiceEndpoint: String?) {
    private val mWeatherServiceInterface: WeatherServiceInterface
    fun getConditionsFor(name: String?): Call<ConditionsServiceResponse?>? {
        return mWeatherServiceInterface.getConditions(name)
    }

    init {
        mWeatherServiceInterface = buildRetrofit(weatherServiceEndpoint)
            .create(WeatherServiceInterface::class.java)
    }
}