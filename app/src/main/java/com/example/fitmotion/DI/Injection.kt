package com.example.fitmotion.DI

import android.content.Context
import com.example.fitmotion.Sensor.Api.CsvApiConfig
import com.example.fitmotion.Sensor.Repo.SensorRepository
import com.example.fitmotion.Signin.SigninApiConfig
import com.example.fitmotion.Signin.SigninRepository
import com.example.fitmotion.Signup.SignupApiConfig
import com.example.fitmotion.Signup.SignupRepository
import com.example.fitmotion.UserHelper.UserPreference
import com.example.fitmotion.UserHelper.UserRepository
import com.example.fitmotion.UserHelper.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideSignupRepository(): SignupRepository {
        val apiService = SignupApiConfig.getSignupApiService()
        return SignupRepository.getInstance(apiService)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideSigninRepository(): SigninRepository{
        val apiService = SigninApiConfig.getSigninApiService()
        return SigninRepository.getInstance(apiService)
    }

    fun provideSensorRepository(context: Context): SensorRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = CsvApiConfig.getCsvApiService(user.token)
        return SensorRepository.getInstance(apiService, pref)
    }
}