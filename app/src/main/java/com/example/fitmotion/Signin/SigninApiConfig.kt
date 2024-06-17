package com.example.fitmotion.Signin

import com.example.fitmotion.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SigninApiConfig {
    fun getSigninApiService(): SigninApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val mySecretUrl = BuildConfig.BASE_URL
        val retrofit = Retrofit.Builder()
            .baseUrl(mySecretUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(SigninApiService::class.java)
    }
}