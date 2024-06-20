package com.example.fitmotion.weekly

data class WeeklyRecord(
    val walk_min: Double,
    val jogging_min: Double,
    val stand_min: Double,
    val sit_min: Double,
    val downstair_min: Double,
    val upstair_min: Double
)

data class WeeklyResponse(
    val code: Int,
    val data: WeeklyRecord
)