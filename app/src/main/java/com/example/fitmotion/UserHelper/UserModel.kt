package com.example.fitmotion.UserHelper

data class UserModel (
    val username: String,
    val token: String,
    val isLogin: Boolean = false,
    val isScreening: Boolean = false
)