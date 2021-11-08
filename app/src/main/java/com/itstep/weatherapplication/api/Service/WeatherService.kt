package com.itstep.weatherapplication.api.Service

import com.itstep.weatherapplication.api.Model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherService {

    interface WeatherService {
        @GET("data/2.5/weather?")
        fun getCurrentWeatherData(@Query("q") q: String, @Query("APPID") app_id: String): Call<WeatherResponse>
    }

}