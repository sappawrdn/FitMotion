package com.example.fitmotion

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.fitmotion.Factory.ViewModelFactory
import com.example.fitmotion.Welcome.WelcomeActivity
import com.example.fitmotion.databinding.FragmentProfileBinding
import com.example.fitmotion.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the edit button from the layout
        val editButton = binding.buttonEdit
        val logoutButton = binding.buttonLogout

        logoutButton.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.logout()

                // Mulai WelcomeActivity setelah logout
                val intent = Intent(activity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        // Set click listener for the edit button
        editButton.setOnClickListener {
            // Intent to start EditProfileActivity
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        // If needed, you can add factory methods or other companion object functions here
    }
}
