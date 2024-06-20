package com.example.fitmotion.Profile

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProfilePic::class], version = 1)

abstract class ProfilePicDatabase: RoomDatabase() {
    abstract fun profilePicDao(): ProfilePicDao
}