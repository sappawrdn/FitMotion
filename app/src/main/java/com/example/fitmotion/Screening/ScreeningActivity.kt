package com.example.fitmotion.Screening

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitmotion.R
import com.example.fitmotion.Sensor.Worker.dataStore
import com.example.fitmotion.UserHelper.UserPreference
import com.example.fitmotion.UserHelper.UserRepository
import com.example.fitmotion.core.CoreApiConfig
import com.example.fitmotion.databinding.ActivityScreeningBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScreeningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScreeningBinding
    private val calendar: Calendar = Calendar.getInstance()

    private val userRepository: UserRepository by lazy {
        UserRepository.getInstance(UserPreference.getInstance(this.dataStore))
    }

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

            val height = binding.heightEditText.text.toString().toInt()
            val weight = binding.weightEditText.text.toString().toInt()
            val birth = binding.dateEditText.text.toString()
            val sexInput = binding.genderAutoComplete.text.toString()
            val goal = "A"
            val commitmentInput = binding.commitAutoComplete.text.toString()

            val sex = when (sexInput) {
                "Female" -> "F"
                "Male" -> "M"
                else -> ""
            }

            val commitment = when (commitmentInput){
                "5 min / day" -> "A"
                "10 min / day" -> "B"
                "15 min / day" -> "C"
                "30 min / day" -> "D"
                "45 min / day" -> "E"
                "60 min / day" -> "F"
                "90 min / day" -> "G"
                else -> ""
            }

            val user = ScreeningUser(
                height = height,
                weight = weight,
                birth = birth,
                sex = sex,
                goal = goal,
                commitment = commitment
            )

            sendUserData(user)


            val intent = Intent(this@ScreeningActivity, SetGoalsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun updateLabel() {
        val myFormat = "yyy-MM-dd" // Ubah format menjadi dd/MM/yy
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.dateEditText.setText(dateFormat.format(calendar.time))
    }

    private fun sendUserData(user: ScreeningUser) {
        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(this@ScreeningActivity.dataStore)
            val userModel = userPreference.getSession().first()
            val token = userModel.token

            val service = ScreeningApiConfig().getScreeningApiService(token)
            val call = service.updateUser(user)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("ScreeningCheck", "Status: ${response.message()}")
                    } else {
                        Log.e("ScreeningCheck", "Response not successful")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@ScreeningActivity, "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}