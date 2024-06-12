package com.example.fitmotion

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.fitmotion.databinding.ActivityScreeningBinding

class ScreeningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScreeningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreeningBinding.inflate(layoutInflater)

        val illness = resources.getStringArray(R.array.illness)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, illness)
        binding.illnessAutoComplete.setAdapter(arrayAdapter)
        setContentView(binding.root)

    }
}