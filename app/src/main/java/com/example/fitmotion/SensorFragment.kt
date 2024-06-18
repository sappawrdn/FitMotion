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
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.example.fitmotion.Sensor.SensorData
import com.example.fitmotion.Sensor.SensorRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SensorFragment : Fragment(), SensorEventListener {

    //variabel sensor

    private lateinit var sensorManager: SensorManager
    private val attitudeReading = FloatArray(3)
    private val gravityReading = FloatArray(3)
    private val gyroscopeReading = FloatArray(3)
    private val accelerometerReading = FloatArray(3)

    private val handler = Handler(Looper.getMainLooper())
    private val saveInterval = 20L

    private val uploadInterval = 5 * 60 * 1000L

    private val checkHandler = Handler(Looper.getMainLooper())
    private val checkInterval = 1000L // 1 detik

    // Counter untuk jumlah data yang disimpan
    private var dataCounter = 0

    //variabel room database untuk sensor
    private lateinit var sensordatabase: SensorRoomDatabase

    var isSensorActive = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sensor, container, false)

        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensordatabase = SensorRoomDatabase.getDatabase(requireContext())

        val toggleButton: Button? = view?.findViewById(R.id.toggle_button)

        toggleButton?.setOnClickListener {
            if (isSensorActive) {
                // Change to the new background
                toggleButton.setBackgroundResource(R.drawable.ic_play)
                sensorManager.unregisterListener(this)
                stopSavingDataPeriodically()
            } else {
                toggleButton.setBackgroundResource(R.drawable.ic_stop)
                registerSensors()
                startSavingDataPeriodically()
        }
            isSensorActive = !isSensorActive
        }


        startCheckingData()
        checkSensorAvailability(Sensor.TYPE_GAME_ROTATION_VECTOR)
        checkSensorAvailability(Sensor.TYPE_GRAVITY)
        checkSensorAvailability(Sensor.TYPE_GYROSCOPE)
        checkSensorAvailability(Sensor.TYPE_LINEAR_ACCELERATION)

        return view
    }

    private fun startSavingDataPeriodically() {
        val saveDataRunnable = object : Runnable {
            override fun run() {
                saveSensorData()
                handler.postDelayed(this, saveInterval)
            }
        }
        handler.post(saveDataRunnable)
    }

    private fun stopSavingDataPeriodically() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun saveSensorData() {
        val sensorData = SensorData().apply {
            attitudepitch = convertToPlainString(attitudeReading[0])
            attituderoll = convertToPlainString(attitudeReading[1])
            attitudeazimuth = convertToPlainString(attitudeReading[2])
            gravityx = convertToPlainString(gravityReading[0])
            gravityy = convertToPlainString(gravityReading[1])
            gravityz = convertToPlainString(gravityReading[2])
            rotationratex = convertToPlainString(gyroscopeReading[0])
            rotationratey = convertToPlainString(gyroscopeReading[1])
            rotationratez = convertToPlainString(gyroscopeReading[2])
            useraccelerationx = convertToPlainString(accelerometerReading[0])
            useraccelarationy = convertToPlainString(accelerometerReading[1])
            useraccelerationz = convertToPlainString(accelerometerReading[2])
        }

        Log.d("SensorData", "Prepared SensorData object: $sensorData")
        lifecycleScope.launch(Dispatchers.IO) {
            sensordatabase.sensorDao().insert(sensorData)
            dataCounter++
        }
    }

    private fun convertToPlainString(value: Float): String {
        return BigDecimal(value.toString()).toPlainString()
    }

    private fun registerSensors() {
        val sensors = listOf(
            Sensor.TYPE_GAME_ROTATION_VECTOR,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_LINEAR_ACCELERATION
        )

        sensors.forEach { sensorType ->
            sensorManager.getDefaultSensor(sensorType)?.also { sensor ->
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    private fun checkSensorAvailability(sensorType: Int) {
        val sensor = sensorManager.getDefaultSensor(sensorType)
        if (sensor != null) {
            Log.d("SensorAvailability", "Sensor with type $sensorType is available on this device.")
        } else {
            Log.d("SensorAvailability", "Sensor with type $sensorType is not available on this device.")
        }
    }

    private fun startCheckingData() {
        val checkDataRunnable = object : Runnable {
            override fun run() {
                Log.d("DataCounter", "Number of rows inserted in the last second: $dataCounter")
                dataCounter = 0 // Reset counter
                checkHandler.postDelayed(this, checkInterval)
            }
        }
        checkHandler.post(checkDataRunnable)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if(event == null){
            return
        }

        if (event.sensor.type == Sensor.TYPE_GAME_ROTATION_VECTOR){
            System.arraycopy(event.values, 0, attitudeReading, 0, attitudeReading.size)
        }else if (event.sensor.type == Sensor.TYPE_GRAVITY){
            System.arraycopy(event.values, 0, gravityReading, 0, gravityReading.size)
        }else if (event.sensor.type == Sensor.TYPE_GYROSCOPE){
            System.arraycopy(event.values, 0, gyroscopeReading, 0, gyroscopeReading.size)
        }else if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION){
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("sensor Accuracy", "Sensor Accuracy changed: $accuracy")
    }

}