package com.example.fitmotion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmotion.Weather.WeatherApiService
import com.example.fitmotion.Weather.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchWeatherData("London")

        val activities = listOf(
            ActivitiesItem(R.drawable.sit, "sit", "60 min"),
            ActivitiesItem(R.drawable.stand, "stand", "60 min"),
            ActivitiesItem(R.drawable.walk, "walk", "60 min"),
            ActivitiesItem(R.drawable.sit, "jogging", "60 min"),
            ActivitiesItem(R.drawable.stand, "Up Stairs", "60 min"),
            ActivitiesItem(R.drawable.walk, "Down Stairs", "60 min")
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_activities)
        val adapter = ActivitiesAdapter(activities)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        val tvDetails = view.findViewById<TextView>(R.id.tv_details)
        tvDetails.setOnClickListener {
            startActivity(Intent(requireContext(), DetailsActivity::class.java))
        }
    }

    private fun createRetrofitService(): WeatherApiService {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()

        return retrofit.create(WeatherApiService::class.java)
    }


    private fun fetchWeatherData(cityName : String){
        val service = createRetrofitService()
        val response = service.getWeatherData("London", "f1869f276bc88084c34603026e4df4da", "metric")
        response.enqueue(object : Callback<WeatherResponse>{
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val temperature = responseBody.main?.temp?.toInt().toString()
                        val condition = responseBody.weather?.firstOrNull()?.main ?: "unknown"

                        Log.d("WeatherData", "$temperature and $condition")

                        view?.findViewById<TextView>(R.id.title_weather)?.text = "$temperature°C"
                        view?.findViewById<TextView>(R.id.text_weather)?.text = condition
                        view?.findViewById<TextView>(R.id.text_weather_city)?.text = cityName
                    } else {
                        Log.e("WeatherData", "Response body is null")
                    }
                } else {
                    Log.e("WeatherData", "Response not successful")
                }

            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("WeatherData", "API call failed", t)
            }

        })
    }
}