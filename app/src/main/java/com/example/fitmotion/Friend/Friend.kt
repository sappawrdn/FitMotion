package com.example.fitmotion.Friend

import com.google.gson.annotations.SerializedName

//data class FriendRequest(
//    @SerializedName("requestId")
//    val requestId: Int,
//    @SerializedName("senderUsername")
//    val senderUsername: String,
//    @SerializedName("sentAt")
//    val sentAt: String
//)

data class Friend(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String
)