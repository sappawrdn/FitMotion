package com.example.fitmotion.Screening

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
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

        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, gender)
        binding.genderAutoComplete.setAdapter(arrayAdapter)
        setContentView(binding.root)

        val commitment = resources.getStringArray(R.array.commitment)
        val commitmentArrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, commitment)
        binding.commitAutoComplete.setAdapter(commitmentArrayAdapter)
        setContentView(binding.root)


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

        binding.buttonContinue.setOnClickListener{
            val intent = Intent(this@ScreeningActivity, SetGoalsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yy" // Ubah format menjadi dd/MM/yy
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.dateEditText.setText(dateFormat.format(calendar.time))
    }
}