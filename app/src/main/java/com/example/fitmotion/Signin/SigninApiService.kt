package com.example.fitmotion.Signin

import com.example.fitmotion.Signup.SignupRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SigninApiService {
    @FormUrlEncoded
    @POST("auth/login")

    suspend fun signin(@Body signinRequest: SigninRequest): SigninResponse
}