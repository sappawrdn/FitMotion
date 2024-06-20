package com.example.fitmotion.Friend

data class FriendRequest(
    val requestId: Int,
    val senderUsername: String,
    val sentAt: String
)

data class Friend(
    val username: String,
    val friendSince: String
)