package com.example.fitmotion.Welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitmotion.R
import com.example.fitmotion.databinding.ActivityMainBinding
import com.example.fitmotion.databinding.ActivityWelcomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class WelcomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_welcome)

    }
}