package com.example.fitmotion.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitmotion.DI.Injection
import com.example.fitmotion.Signup.SignupRepository
import com.example.fitmotion.Signup.SignupViewModel
import com.example.fitmotion.UserHelper.UserRepository
import com.example.fitmotion.main.MainViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val signupRepository: SignupRepository
): ViewModelProvider.NewInstanceFactory()
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(signupRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.provideSignupRepository()
                ).also { INSTANCE = it }
            }
        }
    }
}