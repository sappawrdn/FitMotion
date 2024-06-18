package com.example.fitmotion.Signin

import com.google.gson.annotations.SerializedName

data class SigninRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)