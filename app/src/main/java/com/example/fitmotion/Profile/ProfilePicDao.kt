package com.example.fitmotion.Profile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ProfilePicDao {
    @Insert
    suspend fun insert(profilepic: ProfilePic)

    @Query("SELECT * FROM profilepic WHERE id = :id")
    suspend fun getProfile(id: Int): ProfilePic?

    @Query("DELETE FROM profilepic WHERE id = :id")
    suspend fun deleteProfile(id: Int)
}