package com.example.fitmotion.Signup

class SignupRepository private constructor(
    private val signupApiService: SignupApiService
) {
    suspend fun signupRepo(username: String, email: String, password: String): SignupResponse{
        return signupApiService.signup(username, email, password)
    }

    companion object{
        @Volatile
        private var INSTANCE: SignupRepository? = null

        fun getInstance(signupApiService: SignupApiService): SignupRepository{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: SignupRepository(signupApiService).also { INSTANCE = it }
            }
        }
    }

}