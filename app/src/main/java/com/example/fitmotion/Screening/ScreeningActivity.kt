package com.example.fitmotion.Screening

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitmotion.R
import com.example.fitmotion.databinding.ActivityScreeningBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScreeningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScreeningBinding
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gender = resources.getStringArray(R.array.gender)
        val genderAdapter = ArrayAdapter(this, R.layout.dropdown_menu, gender)
        binding.genderAutoComplete.setAdapter(genderAdapter)

        val commitment = resources.getStringArray(R.array.commitment)
        val commitmentAdapter = ArrayAdapter(this, R.layout.dropdown_menu, commitment)
        binding.commitAutoComplete.setAdapter(commitmentAdapter)

        setupDatePicker()

        binding.buttonContinue.setOnClickListener{
            if (isValidInput()) {
                // Ambil data dari input
                val dateOfBirth = binding.dateEditText.text.toString()
                val gender = binding.genderAutoComplete.text.toString()
                val commitment = binding.commitAutoComplete.text.toString()
                val height = binding.heightEditText.text.toString()
                val weight = binding.weightEditText.text.toString()

                // Kirim data ke SetGoalsActivity
                val intent = Intent(this@ScreeningActivity, SetGoalsActivity::class.java)
                intent.putExtra("dateOfBirth", dateOfBirth)
                intent.putExtra("gender", gender)
                intent.putExtra("commitment", commitment)
                intent.putExtra("height", height)
                intent.putExtra("weight", weight)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDatePicker() {
        val date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        binding.dateEditText.setOnClickListener {
            DatePickerDialog(this,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd" // Ubah format menjadi yyyy-MM-dd atau sesuai kebutuhan
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.dateEditText.setText(dateFormat.format(calendar.time))
    }

    private fun isValidInput(): Boolean {
        val dateOfBirth = binding.dateEditText.text.toString()
        val gender = binding.genderAutoComplete.text.toString()
        val commitment = binding.commitAutoComplete.text.toString()
        val height = binding.heightEditText.text.toString()
        val weight = binding.weightEditText.text.toString()

        return dateOfBirth.isNotBlank() && gender.isNotBlank() && commitment.isNotBlank()
                && height.isNotBlank() && weight.isNotBlank()
    }
}
