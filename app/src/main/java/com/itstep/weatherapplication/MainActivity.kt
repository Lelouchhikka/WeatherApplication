package com.itstep.weatherapplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.itstep.weatherapplication.api.Model.WeatherResponse
import com.itstep.weatherapplication.api.Service.WeatherService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.itstep.weatherapplication.dao.App

import com.itstep.weatherapplication.dao.AppDatabase
import com.itstep.weatherapplication.dao.Weather
import com.itstep.weatherapplication.dao.WeatherDao


class MainActivity : AppCompatActivity() {

    companion object {

        var BaseUrl = "http://api.openweathermap.org/"
        var AppId = "93215021eb1d1b4f20a054773f0577cf"
        var lat = "35"
        var lon = "139"
    }
    private var repoText:List<String>?=null
    private var weatherData: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherData = findViewById(R.id.textView)
        findViewById<View>(R.id.button).setOnClickListener {
            if(isNetworkAvailable(this)){
                getCurrentData()
            }else{
                val weather:Weather?=getWeather()
                if(weather==null){
                weatherData?.text=""
                }else{


                val stringBuilder = "Country: " +
                        weather?.country +
                        "\n" +
                        "Temperature: " +
                        weather?.temparute +
                        "\n" +
                        "Temperature(Min): " +
                        weather?.temperatureMin +
                        "\n" +
                        "Temperature(Max): " +
                        weather?.temperatureMax +
                        "\n" +
                        "Humidity: " +
                        weather?.humdity +
                        "\n" +
                        "Pressure: " +
                        weather?.pressure
                weatherData?.text=stringBuilder
                }
            }
            }
    }
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun saveWeather(weather: Weather){
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "weather.db"
        ).allowMainThreadQueries().build()
       val weatherDao=db.WeatherDao()
        weatherDao.insert(weather)
       Log.e("save","success")

    }
    fun getWeather(): Weather? {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "weather.db"
        ).allowMainThreadQueries().build()
        val weatherDao=db.WeatherDao()
        val weathers: List<Weather?> = weatherDao.getAll()
        if(weathers.size-1<0){
            return null
        }else {


            return weathers.get(weathers.size - 1)
        }
    }

    internal fun getCurrentData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val cityName:EditText=findViewById(R.id.ed_name)
        val service = retrofit.create(WeatherService.WeatherService::class.java)
        val call = service.getCurrentWeatherData(cityName.text.toString(), AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val stringBuilder = "Country: " +
                            weatherResponse.sys!!.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main!!.temp +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main!!.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main!!.temp_max +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main!!.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main!!.pressure

                        val weather=Weather()
                        weather.country=weatherResponse.sys!!.country
                        weather.temparute=weatherResponse.main!!.temp.toString()
                        weather.temperatureMin=weatherResponse.main!!.temp_min.toString()
                        weather.temperatureMax=weatherResponse.main!!.temp_max.toString()
                        weather.humdity=weatherResponse.main!!.humidity.toString()
                        weather.pressure=weatherResponse.main!!.pressure.toString()

                    saveWeather(weather)
                    weatherData!!.text = stringBuilder
                    Log.e("city",stringBuilder)
                }else{
                    weatherData!!.text=""
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                weatherData!!.text = t.message
            }
        })
    }

}
