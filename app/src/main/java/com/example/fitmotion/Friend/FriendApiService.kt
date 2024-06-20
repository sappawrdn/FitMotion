package com.example.fitmotion.Friend

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendApiService {
    @GET("/api/v1/friends/invite/{friend_username}")
    fun sendFriendRequest(@Query("friend_username") friendUsername: String): Call<ResponseBody>

    @GET("/api/v1/friends/accept")
    fun acceptFriendRequest(@Query("request_id") requestId: Int): Call<ResponseBody>

    @GET("/api/v1/users/requests")
    fun getFriendRequests(): Call<List<FriendRequest>>

    @GET("/api/v1/users/friends")
    fun getFriendsList(): Call<List<Friend>>
}