package com.example.fitmotion.Screening

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PATCH

interface ScreeningApiService {
    @PATCH("api/v1/users/")
    @Headers("accept: application/json", "Content-Type: application/json")
    fun updateUser(@Body screeninguser:ScreeningUser): Call<Void>
}