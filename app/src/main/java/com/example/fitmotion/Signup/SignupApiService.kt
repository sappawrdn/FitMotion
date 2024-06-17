package com.example.fitmotion.Signup

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SignupApiService {
    @POST("api/v1/users/")
    suspend fun signup(@Body signupRequest: SignupRequest): SignupResponse
}