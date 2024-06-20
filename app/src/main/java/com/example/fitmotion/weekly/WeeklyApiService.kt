package com.example.fitmotion.weekly

import com.example.fitmotion.daily.DailyResponse
import retrofit2.Call
import retrofit2.http.GET

interface WeeklyApiService {
    @GET("api/v1/activities/weekly")
    fun getWeeklyActivities(): Call<WeeklyResponse>
}