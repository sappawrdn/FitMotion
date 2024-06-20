package com.example.fitmotion.core

import retrofit2.Call
import retrofit2.http.GET

interface CoreApiService {

    @GET("api/v1/screening/health-check/")
    fun getHealthCheck(): Call<CoreApiResponse>
}