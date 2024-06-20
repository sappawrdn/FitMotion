package com.example.fitmotion.core

import com.google.gson.annotations.SerializedName

data class CoreApiResponse(
    @SerializedName("status") val status: String
)