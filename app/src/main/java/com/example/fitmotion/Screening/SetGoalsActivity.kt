package com.example.fitmotion.Screening

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitmotion.R
import com.example.fitmotion.UserHelper.UserPreference
import com.example.fitmotion.UserHelper.UserRepository
import com.example.fitmotion.UserHelper.dataStore
import com.example.fitmotion.databinding.ActivityGoalsBinding
import com.example.fitmotion.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SetGoalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalsBinding
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreference = UserPreference.getInstance(dataStore)
        userRepository = UserRepository.getInstance(userPreference)

        binding.containerLostweight.setOnClickListener { setSelected(binding.containerLostweight) }
        binding.containerGetfitter.setOnClickListener { setSelected(binding.containerGetfitter) }

        binding.saveButton.setOnClickListener{
            saveScreeningData()

        }

        binding.previousButton.setOnClickListener {
            val intent = Intent(this@SetGoalsActivity, ScreeningActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSelected(selectedContainer: View) {
        binding.containerLostweight.setBackgroundResource(R.drawable.rounded_corners)
        binding.containerGetfitter.setBackgroundResource(R.drawable.rounded_corners)

        selectedContainer.setBackgroundResource(R.drawable.border_selected)
    }

    private fun saveScreeningData() {
        // Collect the screening data (height and weight)
//        val height = findViewById<EditText>(R.id.height_input).text.toString()
//        val weight = findViewById<EditText>(R.id.weight_input).text.toString()

        // Save the data and update the isScreening flag
        lifecycleScope.launch {
            val userModel = userRepository.getSession().first()
            val updatedUserModel = userModel.copy(isScreening = true)
            Log.d("SetGoalsActivity", "Updating session with isScreening = true")


            userRepository.saveSession(updatedUserModel)

            val updatedUser = userRepository.getSession().first()
            Log.d("SetGoalsActivity", "Session updated. isScreening = ${updatedUser.isScreening}")

            // Navigate to MainActivity after saving the data
            Log.d("SetGoalsActivity", "Navigating to MainActivity")
            val intent = Intent(this@SetGoalsActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}