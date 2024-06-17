package com.example.fitmotion.Signin

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SigninApiService {
    @FormUrlEncoded
    @POST("auth/login")

    suspend fun signin(
        @Field("username") username: String,
        @Field("password") password: String
    ): SigninResponse
}