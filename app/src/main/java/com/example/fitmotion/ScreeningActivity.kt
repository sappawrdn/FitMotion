package com.example.fitmotion

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
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

        val illness = resources.getStringArray(R.array.illness)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, illness)
        binding.illnessAutoComplete.setAdapter(arrayAdapter)
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

    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yy" // Ubah format menjadi dd/MM/yy
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.dateEditText.setText(dateFormat.format(calendar.time))
    }
}