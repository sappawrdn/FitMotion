package com.example.fitmotion.Achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import android.widget.Button
import com.example.fitmotion.R

class DetailDialogFragment : DialogFragment() {
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var okButton: Button

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_DESCRIPTION = "arg_description"

        fun newInstance(title: String, description: String): DetailDialogFragment {
            val fragment = DetailDialogFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DESCRIPTION, description)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_achievement_detail, container, false)

        // Initialize views
        titleTextView = view.findViewById(R.id.popup_title)
        descriptionTextView = view.findViewById(R.id.popup_description)
        okButton = view.findViewById(R.id.button_popup)

        // Set title and description
        val title = arguments?.getString(ARG_TITLE) ?: ""
        val description = arguments?.getString(ARG_DESCRIPTION) ?: ""
        titleTextView.text = title
        descriptionTextView.text = description

        // Set click listener for OK button
        okButton.setOnClickListener {
            dismiss() // Close the dialog
        }

        return view
    }
}