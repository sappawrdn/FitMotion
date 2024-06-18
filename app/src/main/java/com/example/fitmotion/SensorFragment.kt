package com.example.fitmotion

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fitmotion.Sensor.Data.SensorData
import com.example.fitmotion.Sensor.Data.SensorRoomDatabase
import com.example.fitmotion.Sensor.Worker.SensorWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SensorFragment : Fragment(){

    //variabel sensor

    var isSensorActive = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sensor, container, false)

        val toggleButton: Button? = view?.findViewById(R.id.toggle_button)

        toggleButton?.setOnClickListener {
            if (isSensorActive) {
                // Change to the new background
                toggleButton.setBackgroundResource(R.drawable.ic_play)
                SensorWorker.stopPeriodicWork(requireContext())
            } else {
                toggleButton.setBackgroundResource(R.drawable.ic_stop)
                SensorWorker.startPeriodicWork(requireContext())
        }
            isSensorActive = !isSensorActive
        }

        return view
    }

}