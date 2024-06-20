package com.example.fitmotion.daily

data class DailyRecord(
    val walk_min: Double,
    val jogging_min: Double,
    val stand_min: Double,
    val sit_min: Double,
    val downstair_min: Double,
    val upstair_min: Double
)

data class DailyResponse(
    val code: Int,
    val data: DailyRecord
)