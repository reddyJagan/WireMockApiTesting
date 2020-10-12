package com.example.wiremockapitesting.model

import com.google.gson.annotations.SerializedName

class WeatherCondition {
    @SerializedName("name")
    var mName: String? = null

    @SerializedName("city")
    var mCity: String? = null

    @SerializedName("state")
    var mState: String? = null

    @SerializedName("country")
    var mCountry: String? = null
    override fun toString(): String {
        return "WeatherCondition{" +
                "mName='" + mName + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mState='" + mState + '\'' +
                ", mCountry='" + mCountry + '\'' +
                '}'
    }
}