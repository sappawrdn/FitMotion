package com.example.fitmotion.Screening

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fitmotion.R
import com.example.fitmotion.UserHelper.UserPreference
import com.example.fitmotion.UserHelper.UserRepository
import com.example.fitmotion.UserHelper.dataStore
import com.example.fitmotion.databinding.ActivityGoalsBinding


class SetGoalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalsBinding
    private lateinit var userRepository: UserRepository
    private var selectedGoal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreference = UserPreference.getInstance(dataStore)
        userRepository = UserRepository.getInstance(userPreference)

        binding.containerLostweight.setOnClickListener { setSelected(binding.containerLostweight, "Lost Weight") }
        binding.containerGetfitter.setOnClickListener { setSelected(binding.containerGetfitter, "Get Fitter") }


        binding.previousButton.setOnClickListener {
            val intent = Intent(this@SetGoalsActivity, ScreeningActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSelected(selectedContainer: View, goal: String) {
        // Reset all containers to default state
        binding.containerLostweight.setBackgroundResource(R.drawable.rounded_corners)
        binding.containerGetfitter.setBackgroundResource(R.drawable.rounded_corners)

        // Set the selected container
        selectedContainer.setBackgroundResource(R.drawable.border_selected)

        // Update selected goal
        selectedGoal = goal
    }

}
