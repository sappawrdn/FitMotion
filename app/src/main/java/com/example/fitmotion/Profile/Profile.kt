package com.example.fitmotion.Profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profilePic")
data class ProfilePic(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUri: String
)
