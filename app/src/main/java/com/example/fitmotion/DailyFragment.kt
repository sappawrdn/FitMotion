package com.example.fitmotion

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.fitmotion.Sensor.Worker.dataStore
import com.example.fitmotion.UserHelper.UserPreference
import com.example.fitmotion.daily.DailyApiConfig
import com.example.fitmotion.daily.DailyRecord
import com.example.fitmotion.daily.DailyResponse
import com.example.fitmotion.databinding.FragmentDailyBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")


class DailyFragment : Fragment() {

    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        fetchData()
        return binding.root
    }

    private fun fetchData() {
        lifecycleScope.launch { // Use lifecycleScope to launch coroutine
            val userPreference = UserPreference.getInstance(requireContext().dataStore)
            try {
                val userModel = userPreference.getSession().first() // Collect value from flow
                val token = userModel.token

                val apiService = DailyApiConfig().getDailyApiService(token)
                val call = apiService.getDailyActivities()
                call.enqueue(object : Callback<DailyResponse> {
                    override fun onResponse(call: Call<DailyResponse>, response: Response<DailyResponse>) {
                        if (response.isSuccessful) {
                            val activityData = response.body()?.data
                            if (activityData != null) {
                                Log.d("DailyFragment", "Activity Data: $activityData")
                                setupBarChart(activityData)
                            } else {
                                Toast.makeText(requireContext(), "No activity data available", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to get data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DailyResponse>, t: Throwable) {
                        if (isAdded()) {
                            Toast.makeText(requireContext(), "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error fetching session: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBarChart(data: DailyRecord) {
        val barChart = binding.barChart

        val entries = mutableListOf<BarEntry>().apply {
            add(BarEntry(0f, 130f))
            add(BarEntry(1f, 15f))
            add(BarEntry(2f, 10f))
            add(BarEntry(3f, 5f))
            add(BarEntry(4f, 4f))
            add(BarEntry(5f, 4f))
        }


        val dataSet = BarDataSet(entries, "Activity Data")
        dataSet.colors = listOf(
            Color.parseColor("#FFEB3B"),
            Color.parseColor("#FFC107"),
            Color.parseColor("#FF9800"),
            Color.parseColor("#F44336"),
            Color.parseColor("#E91E63")
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.9f
        barChart.data = barData

        barChart.description.isEnabled = false
        barChart.setFitBars(true)

        // Customizing the x-axis
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Sit", "Stand", "Walk", "Jogging", "Upstairs", "Downstairs"))

        // Customizing the left y-axis
        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 210f
        leftAxis.granularity = 30f

        // Customizing the right y-axis
        val rightAxis: YAxis = barChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f

        barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
