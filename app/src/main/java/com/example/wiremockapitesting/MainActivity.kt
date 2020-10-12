package com.example.wiremockapitesting

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wiremockapitesting.model.ConditionsServiceResponse
import com.example.wiremockapitesting.model.WeatherCondition
import com.example.wiremockapitesting.service.WeatherServiceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), View.OnClickListener {
    protected var text: EditText? = null
    protected var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<View>(R.id.button) as Button
        text = findViewById<View>(R.id.editText) as EditText



        button!!.setOnClickListener {
            val location = text!!.text.toString()

            val call: Call<ConditionsServiceResponse?>? =
                weatherServiceManager.getConditionsFor(location)

            call!!.enqueue(object : Callback<ConditionsServiceResponse?> {
                override fun onResponse(
                    call: Call<ConditionsServiceResponse?>,
                    response: Response<ConditionsServiceResponse?>
                ) {
                    val conditionsForCity: List<WeatherCondition>? =
                        response.body()?.conditionsResponse!!.weatherConditions
                    if (conditionsForCity != null) {
                        val out = StringBuffer()
                        for (wc in conditionsForCity) {
                            out.append(wc.toString())
                        }
                        val outText = findViewById<View>(R.id.textView) as TextView
                        outText.text = out.toString()
                    }

                }

                override fun onFailure(
                    call: Call<ConditionsServiceResponse?>,
                    t: Throwable
                ) {
                    Log.d("Response", "onFail: " + t.message)
                }
            })

        }
    }

    private var weatherServiceManager: WeatherServiceManager =
        WeatherServiceManager("http://api.wunderground.com/")

    fun setWeatherServiceManager(mWeatherServiceManager: WeatherServiceManager) {
        weatherServiceManager = mWeatherServiceManager
    }

    override fun onClick(p0: View?) {
        text!!.setText("")
    }

}

