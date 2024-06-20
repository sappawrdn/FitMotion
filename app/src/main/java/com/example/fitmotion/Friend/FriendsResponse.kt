package com.example.fitmotion.Friend

import com.google.gson.annotations.SerializedName


data class FriendsResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val friends: List<Friend>
)