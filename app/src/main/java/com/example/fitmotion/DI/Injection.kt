package com.example.fitmotion.DI

import android.content.Context
import com.example.fitmotion.Signup.SignupApiConfig
import com.example.fitmotion.Signup.SignupRepository
import com.example.fitmotion.UserHelper.UserPreference
import com.example.fitmotion.UserHelper.UserRepository
import com.example.fitmotion.UserHelper.dataStore

object Injection {

    fun provideSignupRepository(): SignupRepository {
        val apiService = SignupApiConfig.getSignupApiService()
        return SignupRepository.getInstance(apiService)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}