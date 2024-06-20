package com.example.fitmotion.Profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profilePic")
data class ProfilePic(
    @PrimaryKey val id: Int,
    val imageUri: String
)
