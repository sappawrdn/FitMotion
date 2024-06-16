package com.example.fitmotion.Signup

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SignupApiService {
    @FormUrlEncoded
    @POST("api/v1/users")
    suspend fun signup(
        @Field("name") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignupResponse
}