package com.example.fitmotion.Signin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.fitmotion.Factory.ViewModelFactory
import com.example.fitmotion.UserHelper.UserPreference
import com.example.fitmotion.UserHelper.UserRepository
import com.example.fitmotion.UserHelper.dataStore
import com.example.fitmotion.databinding.ActivitySigninBinding
import com.example.fitmotion.main.MainActivity

class SigninActivity: AppCompatActivity() {

    private val signinViewModel by viewModels<SigninViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySigninBinding

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreference = UserPreference.getInstance(dataStore)
        userRepository = UserRepository.getInstance(userPreference)


        setupView()
        setupAction()
        observeViewModel()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction(){
        binding.buttonSignin.setOnClickListener {
            val username = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            signinViewModel.signin(username, password)
        }
    }

    private fun observeViewModel(){
        signinViewModel.signinResponse.observe(this, Observer { response ->
            response?.let {
                Log.d("Sign In Response", "login OK")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })

        signinViewModel.errorMessage.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                Log.e("Sign In Response", error)
            }
        })

    }
}