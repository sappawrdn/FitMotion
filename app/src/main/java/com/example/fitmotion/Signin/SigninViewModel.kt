package com.example.fitmotion.Signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitmotion.UserHelper.UserModel
import com.example.fitmotion.UserHelper.UserRepository
import kotlinx.coroutines.launch

class SigninViewModel(
    private val userRepository: UserRepository,
    private val signinRepository: SigninRepository
) : ViewModel(){

    private val _signinResponse = MutableLiveData<SigninResponse>()
    val signinResponse: LiveData<SigninResponse> = _signinResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun signin(username: String, password: String){
        viewModelScope.launch {
            try {
                val response = signinRepository.signinRepo(username, password)
                _signinResponse.postValue(response)
                saveSession(response.username, response.accessToken)
            }catch (e: Exception){
                _errorMessage.postValue("Login failed: ${e.message}")
            }
        }
    }

    private suspend fun saveSession(username: String?, token: String?) {
        if (username != null && token != null) {
            val user = UserModel(username, token, true)
            userRepository.saveSession(user)
        }
    }
}
