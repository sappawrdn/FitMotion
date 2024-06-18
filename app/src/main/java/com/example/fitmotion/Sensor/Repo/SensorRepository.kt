package com.example.fitmotion.Sensor.Repo

import com.example.fitmotion.Sensor.Api.CsvApiService
import com.example.fitmotion.Sensor.Api.CsvResponse
import com.example.fitmotion.UserHelper.UserPreference
import okhttp3.MultipartBody
import retrofit2.Response

class SensorRepository private constructor(
    private val csvApiService: CsvApiService,
    private val userPreference: UserPreference
) {
    suspend fun sensorRepo(file: MultipartBody.Part): Response<CsvResponse> {
        return csvApiService.uploadFile(file)
    }

    companion object {
        @Volatile
        private var INSTANCE: SensorRepository? = null

        fun getInstance(csvApiService: CsvApiService, userPreference: UserPreference): SensorRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SensorRepository(csvApiService, userPreference).also { INSTANCE = it }
            }
        }
    }
}