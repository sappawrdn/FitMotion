package com.example.fitmotion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.fitmotion.Profile.ProfilePic
import com.example.fitmotion.Profile.ProfilePicProvider
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditProfileActivity : AppCompatActivity() {


    private lateinit var btnChangePhoto: Button
    private lateinit var ivEdit: CircleImageView
    private lateinit var btnDeletePhoto: Button
    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    selectedImageUri = it
                    ivEdit.setImageURI(it)
                    saveImageUri(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        ivEdit = findViewById(R.id.iv_edit)
        btnChangePhoto = findViewById(R.id.btn_change_photo)

        btnChangePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImage.launch(intent)
        }

        btnDeletePhoto = findViewById(R.id.btn_delete_photo)

        btnDeletePhoto.setOnClickListener{
            ivEdit.setImageResource(R.drawable.ic_profile)
            deleteImageUri()
        }

        findViewById<ImageButton>(R.id.btn_back_edit).setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun saveImageUri(uri: Uri) {
        val profile = ProfilePic(id = 1, imageUri = uri.toString())
        CoroutineScope(Dispatchers.IO).launch {
            ProfilePicProvider.getDatabase(this@EditProfileActivity).profilePicDao().insert(profile)
        }
    }

    private fun deleteImageUri() {
        CoroutineScope(Dispatchers.IO).launch {
            ProfilePicProvider.getDatabase(this@EditProfileActivity).profilePicDao().deleteProfile(1)
        }
    }
}