package com.example.fitmotion

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fitmotion.databinding.ActivityGoalsBinding

class SetGoalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.containerLostweight.setOnClickListener { setSelected(binding.containerLostweight) }
        binding.containerGetfitter.setOnClickListener { setSelected(binding.containerGetfitter) }
    }

    private fun setSelected(selectedContainer: View) {
        binding.containerLostweight.setBackgroundResource(R.drawable.rounded_corners)
        binding.containerGetfitter.setBackgroundResource(R.drawable.rounded_corners)

        selectedContainer.setBackgroundResource(R.drawable.border_selected)
    }
}