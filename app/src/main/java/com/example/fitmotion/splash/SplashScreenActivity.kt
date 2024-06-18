package com.example.fitmotion.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitmotion.Factory.ViewModelFactory
import com.example.fitmotion.R
import com.example.fitmotion.Welcome.WelcomeActivity
import com.example.fitmotion.main.MainActivity
import com.example.fitmotion.main.MainViewModel

class SplashScreenActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            // Check session status
            viewModel.getSession().observe(this) { user ->
                if (!user.isLogin) {
                    // User is not logged in, navigate to WelcomeActivity
                    startActivity(Intent(this, WelcomeActivity::class.java))
                } else {
                    // User is logged in, navigate to MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                }
                // Finish SplashScreenActivity to prevent it from showing after navigation
                finish()
            }
        }
    }
}