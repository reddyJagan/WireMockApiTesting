package com.example.wiremockapitesting.model

import com.google.gson.annotations.SerializedName

class ConditionsServiceResponse {
    inner class ConditionsResponse {
        @SerializedName("results")
        var weatherConditions: List<WeatherCondition>? = null

        override fun toString(): String {
            return "ConditionsResponse{" +
                    "mWeatherConditions=" + weatherConditions +
                    '}'
        }
    }

    @SerializedName("response")
    var conditionsResponse: ConditionsResponse? = null

    override fun toString(): String {
        return "ConditionsServiceResponse{" +
                "mConditionsResponse=" + conditionsResponse +
                '}'
    }
}