package com.example.fitmotion


import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmotion.Factory.ViewModelFactory
import com.example.fitmotion.Sensor.Worker.dataStore
import com.example.fitmotion.UserHelper.UserPreference
import com.example.fitmotion.Weather.WeatherApiService
import com.example.fitmotion.Weather.WeatherResponse
import com.example.fitmotion.databinding.FragmentHomeBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.fitmotion.Profile.ProfilePicProvider
import com.example.fitmotion.UserHelper.UserRepository
import com.example.fitmotion.core.CoreApiConfig
import com.example.fitmotion.core.CoreApiResponse
import com.example.fitmotion.daily.DailyResponse
import com.example.fitmotion.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    private val userRepository: UserRepository by lazy {
        UserRepository.getInstance(UserPreference.getInstance(requireContext().dataStore))
    }

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            userRepository.getSession().collect { userModel ->
                binding.homeUsername.text = userModel.username
            }
        }

        loadImageUri()

        fetchWeatherData("Medan")
        fetchHealthCheckData()

        val activities = listOf(
            ActivitiesItem(R.drawable.sit, "sit", "130 min"),
            ActivitiesItem(R.drawable.stand, "stand", "15 min"),
            ActivitiesItem(R.drawable.walk, "walk", "10 min"),
            ActivitiesItem(R.drawable.sit, "jogging", "5 min"),
            ActivitiesItem(R.drawable.stand, "Up Stairs", "4 min"),
            ActivitiesItem(R.drawable.walk, "Down Stairs", "4 min")
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

    private fun loadImageUri() {
        CoroutineScope(Dispatchers.IO).launch {
            val profile = ProfilePicProvider.getDatabase(requireContext()).profilePicDao().getProfile(1)
            profile?.let {
                val imageUri = Uri.parse(it.imageUri)
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        requireContext().contentResolver.takePersistableUriPermission(
                            imageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        binding.profileCircle.setImageURI(imageUri)
                    }
                } else {
                    // Handle case where permission is not granted
                    Log.e("HomeFragment", "Permission not granted for reading external storage")
                    // You might want to request permission again or show an explanation
                }
            }
        }
    }

    private fun fetchHealthCheckData() {
        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(requireContext().dataStore)
            try {
                val userModel = userPreference.getSession().first()
                val token = userModel.token

                val service = CoreApiConfig().getCoreApiService(token)
                val call = service.getHealthCheck()
                call.enqueue(object : Callback<CoreApiResponse> {
                    override fun onResponse(call: Call<CoreApiResponse>, response: Response<CoreApiResponse>) {
                        if (response.isSuccessful) {
                            val healthCheckResponse = response.body()
                            healthCheckResponse?.let {
                                // Lakukan sesuatu dengan data health check yang diterima
                                Log.d("HealthCheck", "Status: ${it.status}")
                            }
                        } else {
                            Log.e("HealthCheck", "Response not successful")
                        }
                    }

                    override fun onFailure(call: Call<CoreApiResponse>, t: Throwable) {
                        if (isAdded) {
                            Toast.makeText(requireContext(), "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error fetching session: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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

    private fun fetchWeatherData(cityName: String) {
        val service = createRetrofitService()
        val response = service.getWeatherData(cityName, "f1869f276bc88084c34603026e4df4da", "metric")
        response.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val temperature = responseBody.main?.temp?.toInt().toString()
                        val condition = responseBody.weather?.firstOrNull()?.main ?: "unknown"

                        Log.d("WeatherData", "$temperature and $condition")

                        binding.titleWeather.text = "$temperature°C"
                        binding.textWeather.text = condition
                        binding.textWeatherCity.text = cityName
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