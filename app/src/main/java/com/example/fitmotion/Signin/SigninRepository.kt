package com.example.fitmotion.Signin

import android.hardware.biometrics.BiometricManager.Strings

class SigninRepository private constructor(
    private val signinApiService: SigninApiService
) {
    suspend fun signinRepo(username: String, password: String): SigninResponse{
        return signinApiService.signin(username, password)
    }

    companion object {
        @Volatile
        private var INSTANCE: SigninRepository? = null

        fun getInstance(signinApiService: SigninApiService): SigninRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SigninRepository(signinApiService).also { INSTANCE = it }
            }
        }
    }
}