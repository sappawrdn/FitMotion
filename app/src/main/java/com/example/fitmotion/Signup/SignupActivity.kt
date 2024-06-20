package com.example.fitmotion.Signup

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.fitmotion.Factory.ViewModelFactory
import com.example.fitmotion.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        observeViewModel()
    }

    private fun setupView() {
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

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmpasswordEditText.text.toString()

            val emailValid = isValidEmail(email)
            val passwordsMatch = password == confirmPassword

            // Validate email
            binding.emailEditTextLayout.error =
                if (emailValid) null else "Invalid email format"

            // Validate password match
            binding.confirmpasswordEditTextLayout.error =
                if (passwordsMatch) null else "Passwords do not match"

            // Check all validations
            if (emailValid && passwordsMatch) {
                showProgressBar(true)
                signupViewModel.signup(username, email, password)
            } else {
                binding.signupButton.requestFocus()
            }
        }

        binding.confirmpasswordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = binding.passwordEditText.text.toString()
                val confirmPassword = binding.confirmpasswordEditText.text.toString()
                binding.confirmpasswordEditTextLayout.error =
                    if (password == confirmPassword) null else "Passwords do not match"
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun observeViewModel() {
        signupViewModel.successMessage.observe(this, Observer { message ->
            message?.let { showSuccessDialog(it) }
        })

        signupViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let { showErrorDialog(it) }
        })
    }

    private fun showProgressBar(show: Boolean) {
        binding.progbarSignup.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(errorMessage)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Success")
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                finish()
            }
            create()
            show()
        }
    }
}
