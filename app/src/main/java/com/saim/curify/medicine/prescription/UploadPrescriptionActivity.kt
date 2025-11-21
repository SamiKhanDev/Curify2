package com.saim.curify.medicine.prescription

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.saim.curify.databinding.ActivityUploadPrescriptionBinding
import kotlinx.coroutines.launch
import com.saim.DataSources.CloudinaryUploadHelper.Companion.initializeCloudinary

@AndroidEntryPoint
class UploadPrescriptionActivity: AppCompatActivity() {
    lateinit var binding: ActivityUploadPrescriptionBinding
    private var imageUri: Uri? = null
    private val vm: PrescriptionViewModel by viewModels()

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            Log.d("PrescriptionUpload", "Image picked uri=$uri")
            Glide.with(this).load(uri).into(binding.previewImage)
            binding.previewCard.visibility = android.view.View.VISIBLE
            binding.placeholderText.visibility = android.view.View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ensure Cloudinary is initialized (idempotent)
        initializeCloudinary(this)

        binding.toolbar.setNavigationOnClickListener { finish() }

        // Apply system bar insets to avoid overlap with status/navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.setPadding(
                binding.toolbar.paddingLeft,
                binding.toolbar.paddingTop + systemBars.top,
                binding.toolbar.paddingRight,
                binding.toolbar.paddingBottom
            )
            // Note: No actions container in layout, padding handled by root layout
            insets
        }

        binding.chooseFromGalleryButton.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.uploadButton.setOnClickListener {
            val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Log.e("PrescriptionUpload", "No authenticated user; aborting upload")
                Toast.makeText(this, "Please login to upload", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val uid = user.uid
            val note = binding.noteInput.text?.toString().orEmpty()
            val path = imageUri?.toString() ?: return@setOnClickListener
            Log.d("PrescriptionUpload", "Upload clicked uid=${uid.isNotEmpty()} noteLength=${note.length} uri=$path")
            try {
                val type = contentResolver.getType(imageUri!!)
                Log.d("PrescriptionUpload", "Picked MIME type=$type")
            } catch (t: Throwable) {
                Log.w("PrescriptionUpload", "Could not resolve MIME type: ${t.message}")
            }
            binding.uploadButton.isEnabled = false
            binding.uploadButton.text = "Uploading..."
            vm.uploadAndSave(uid, path, note)
        }

        lifecycleScope.launch {
            vm.isSaved.collect { saved ->
                if (saved == true) {
                    Log.d("PrescriptionUpload", "Firestore save success")
                    Toast.makeText(this@UploadPrescriptionActivity, "Uploaded", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        lifecycleScope.launch {
            vm.error.collect { err ->
                err?.let {
                    Log.e("PrescriptionUpload", "Error: $it")
                    binding.uploadButton.isEnabled = true
                    binding.uploadButton.text = "Upload"
                    Toast.makeText(this@UploadPrescriptionActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

