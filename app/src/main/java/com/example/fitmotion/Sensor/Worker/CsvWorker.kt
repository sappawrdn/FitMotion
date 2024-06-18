package com.example.fitmotion.Sensor.Worker

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fitmotion.Sensor.Api.CsvApiConfig
import com.example.fitmotion.Sensor.Data.SensorRoomDatabase
import com.example.fitmotion.Sensor.Repo.SensorRepository
import com.example.fitmotion.UserHelper.UserPreference
import com.opencsv.CSVWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileWriter

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class CsvWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams){
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = SensorRoomDatabase.getDatabase(applicationContext)
            val sensorDataList = database.sensorDao().getAllSensorData()

            if (sensorDataList.isNotEmpty()) {
                val csvFile = File(applicationContext.filesDir, "sensor_data.csv")
                val writer = CSVWriter(FileWriter(csvFile))

                val header = arrayOf(
                    "Attitude Roll", "Attitude Pitch", "Attitude Azimuth",
                    "Gravity X", "Gravity Y", "Gravity Z",
                    "Rotation Rate X", "Rotation Rate Y", "Rotation Rate Z",
                    "User Acceleration X", "User Acceleration Y", "User Acceleration Z"
                )

                writer.writeNext(header)

                for (data in sensorDataList) {
                    val line = arrayOf(
                        data.attituderoll, data.attitudepitch, data.attitudeazimuth,
                        data.gravityx, data.gravityy, data.gravityz,
                        data.rotationratex, data.rotationratey, data.rotationratez,
                        data.useraccelerationx, data.useraccelarationy, data.useraccelerationz
                    )
                    writer.writeNext(line)
                }

                writer.close()

                // Upload CSV file
                val uploadResult = uploadCsvFile(csvFile)
                if (uploadResult) {
                    // Clear the table after exporting
                    database.sensorDao().clearTable()
                } else {
                    return@withContext Result.retry()
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("CsvExportWorker", "Error exporting data to CSV", e)
            Result.failure()
        }
    }

    private suspend fun uploadCsvFile(file: File): Boolean {
        return try {
            val userPreference = UserPreference.getInstance(applicationContext.dataStore)
            val userModel = userPreference.getSession().first()
            val token = userModel.token

            val csvApiService = CsvApiConfig.getCsvApiService(token)
            val repository = SensorRepository.getInstance(csvApiService, userPreference)
            Log.e("TOKEN", "token nya adalah: $token")

            val requestFile = RequestBody.create("text/csv".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response = repository.sensorRepo(body)
            response.message() == "File uploaded successfully"
        } catch (e: HttpException) {
            Log.e("CsvExportWorker", "HTTP error during file upload", e)
            false
        } catch (e: Exception) {
            Log.e("CsvExportWorker", "Error during file upload", e)
            false
        }
    }

}