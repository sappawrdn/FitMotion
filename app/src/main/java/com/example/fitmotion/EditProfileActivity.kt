package com.example.fitmotion


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
    private lateinit var btnSave: Button
    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    selectedImageUri = it
                    ivEdit.setImageURI(it)
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // We have permission, persist URI permission
                        contentResolver.takePersistableUriPermission(
                            it,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                    } else {
                        // Handle the case where permission is not granted
                        // You may want to show an explanation or request permission again
                    }
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. You can proceed with the image picking.
                pickImageFromGallery()
            } else {
                // Permission denied. Show an explanation to the user.
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        ivEdit = findViewById(R.id.iv_edit)
        btnChangePhoto = findViewById(R.id.btn_change_photo)

        btnChangePhoto.setOnClickListener {
            pickImageFromGallery()
        }

        btnDeletePhoto = findViewById(R.id.btn_delete_photo)

        btnDeletePhoto.setOnClickListener{
            ivEdit.setImageResource(R.drawable.ic_profile)
            deleteImageUri()
        }

        btnSave = findViewById(R.id.button_save)

        btnSave.setOnClickListener{
            selectedImageUri?.let { uri ->
                saveImageUri(uri)
            }
        }


        findViewById<ImageButton>(R.id.btn_back_edit).setOnClickListener {
            onBackPressed()
        }
    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        pickImage.launch(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun saveImageUri(uri: Uri) {
        val profile = ProfilePic(imageUri = uri.toString())
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