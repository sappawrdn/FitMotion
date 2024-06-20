package com.example.fitmotion.Profile

import android.content.Context
import androidx.room.Room

object ProfilePicProvider {
    private var INSTANCE: ProfilePicDatabase? = null

    fun getDatabase(context: Context): ProfilePicDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ProfilePicDatabase::class.java,
                "app_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}