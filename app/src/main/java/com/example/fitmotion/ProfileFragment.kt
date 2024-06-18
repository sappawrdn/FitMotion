package com.example.fitmotion

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Set click listener for the edit button
        editButton.setOnClickListener {
            // Intent to start EditProfileActivity
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLogoutConfirmationDialog() {
        // Inflate dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)

        // Create AlertDialog.Builder
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        // Create AlertDialog
        val dialog = builder.create()

        // Find views in the custom layout
        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)

        // Set click listener for Yes button
        btnYes.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.logout()

                // Start WelcomeActivity after logout
                val intent = Intent(activity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                dialog.dismiss() // Dismiss the dialog after logout
            }
        }

        // Set click listener for No button
        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    companion object {
        // If needed, you can add factory methods or other companion object functions here
    }
}
