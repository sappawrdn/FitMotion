package com.example.fitmotion.Sensor.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SensorData::class], version = 1)

abstract class SensorRoomDatabase: RoomDatabase() {
    abstract fun sensorDao(): SensorDao

    companion object {
        @Volatile
        private var INSTANCE: SensorRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): SensorRoomDatabase {
            if (INSTANCE == null) {
                synchronized(SensorRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SensorRoomDatabase::class.java, "tes_data0"
                    )
                        .build()
                }
            }
            return INSTANCE as SensorRoomDatabase
        }
    }
}