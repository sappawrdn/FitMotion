package com.example.fitmotion.Signin

import com.google.gson.annotations.SerializedName

data class SigninResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
