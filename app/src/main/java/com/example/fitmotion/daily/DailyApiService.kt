package com.example.fitmotion.daily

import retrofit2.Call
import retrofit2.http.GET

interface DailyApiService {
    @GET("api/v1/activities/daily")
    fun getDailyActivities(): Call<DailyResponse>
}