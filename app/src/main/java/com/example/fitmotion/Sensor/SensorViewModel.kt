package com.example.fitmotion.Sensor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitmotion.Sensor.Api.CsvResponse
import com.example.fitmotion.Sensor.Repo.SensorRepository
import com.example.fitmotion.Signin.SigninRepository
import com.example.fitmotion.Signin.SigninResponse
import com.example.fitmotion.UserHelper.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class SensorViewModel (
    private val sensorRepository: SensorRepository
) : ViewModel() {

    private val _csvResponse = MutableLiveData<Response<CsvResponse>>()
    val csvResponse: LiveData<Response<CsvResponse>> = _csvResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun sensorPost(file: MultipartBody.Part){
        viewModelScope.launch {
            try {
                val response = sensorRepository.sensorRepo(file)
                _csvResponse.postValue(response)
//                saveSession(response.username, response.accessToken)
            }catch (e: Exception){
                _errorMessage.postValue("Login failed: ${e.message}")
            }
        }
    }

}