package com.example.fitmotion.Sensor.Worker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.fitmotion.Sensor.Api.CsvApiConfig
import com.example.fitmotion.Sensor.Api.CsvResponse
import com.example.fitmotion.Sensor.Data.SensorData
import com.example.fitmotion.Sensor.Data.SensorRoomDatabase
import com.example.fitmotion.Sensor.helper.CsvHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.FileWriter
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

class SensorWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params),
    SensorEventListener{

    private lateinit var sensorManager: SensorManager
    private val attitudeReading = FloatArray(3)
    private val gravityReading = FloatArray(3)
    private val gyroscopeReading = FloatArray(3)
    private val accelerometerReading = FloatArray(3)

    private lateinit var sensordatabase: SensorRoomDatabase

    override suspend fun doWork(): Result {
        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensordatabase = SensorRoomDatabase.getDatabase(applicationContext)

        registerSensors()

        // Simulate saving data
        saveSensorData()

        // Simulate uploading data
        uploadSensorData()

        return Result.success()
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

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            return
        }

        when (event.sensor.type) {
            Sensor.TYPE_GAME_ROTATION_VECTOR -> System.arraycopy(event.values, 0, attitudeReading, 0, attitudeReading.size)
            Sensor.TYPE_GRAVITY -> System.arraycopy(event.values, 0, gravityReading, 0, gravityReading.size)
            Sensor.TYPE_GYROSCOPE -> System.arraycopy(event.values, 0, gyroscopeReading, 0, gyroscopeReading.size)
            Sensor.TYPE_LINEAR_ACCELERATION -> System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("sensor Accuracy", "Sensor Accuracy changed: $accuracy")
    }

    private fun convertToPlainString(value: Float): String {
        return BigDecimal(value.toString()).toPlainString()
    }

    private suspend fun saveSensorData() {
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
        withContext(Dispatchers.IO) {
            sensordatabase.sensorDao().insert(sensorData)
        }
    }

    private suspend fun uploadSensorData() {
        withContext(Dispatchers.IO) {
            try {
                val csvFile = createCsvFile()
                val requestBody = RequestBody.create("text/csv".toMediaTypeOrNull(), csvFile)
                val body = MultipartBody.Part.createFormData("file", csvFile.name, requestBody)

                val response: Response<CsvResponse> = CsvApiConfig.getCsvApiService().uploadFile(body)

                if (response.isSuccessful) {
                    Log.d("UploadSensorData", "Successfully uploaded sensor data.")
                } else {
                    Log.d("UploadSensorData", "Failed to upload sensor data. Response code: ${response.code()}")
                }

                csvFile.delete() // Delete the file after upload
            } catch (e: Exception) {
                Log.e("UploadSensorData", "Error uploading sensor data", e)
            }
        }
    }

    private fun createCsvFile(): File {
        val csvFile = File(applicationContext.cacheDir, "sensor_data.csv")
        val data = sensordatabase.sensorDao().getAllSensorData()

        val csvContent = CsvHelper.toCsv(data)

        csvFile.writeText(csvContent)

        return csvFile
    }

    companion object {
        private const val WORK_TAG = "sensor_worker"

        fun startPeriodicWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<SensorWorker>(
                repeatInterval = 1, // Interval in hours
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag(WORK_TAG)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)
        }

        fun stopPeriodicWork(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_TAG)
        }
    }

}