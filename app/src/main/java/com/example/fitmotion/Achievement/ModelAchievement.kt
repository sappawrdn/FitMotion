package com.example.fitmotion.Achievement

data class ModelAchievement(
    val title: String,
    val isAchieved: Boolean,
    val imageName: String,
    val descriptionResId: Int  // Tambahkan properti description
)
