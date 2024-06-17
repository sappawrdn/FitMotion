package com.example.fitmotion.Signup

class SignupRepository private constructor(
    private val signupApiService: SignupApiService
) {
    suspend fun signupRepo(username: String, email: String, password: String): SignupResponse{
        val signupRequest = SignupRequest(username, email, password)
        return signupApiService.signup(signupRequest)
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