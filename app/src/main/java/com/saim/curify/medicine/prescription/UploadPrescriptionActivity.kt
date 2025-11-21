package com.saim.curify.medicine.prescription

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import android.content.ContentResolver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.saim.curify.databinding.ActivityUploadPrescriptionBinding
import kotlinx.coroutines.launch
import com.saim.DataSources.CloudinaryUploadHelper.Companion.initializeCloudinary
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class UploadPrescriptionActivity: AppCompatActivity() {
    lateinit var binding: ActivityUploadPrescriptionBinding
    private var imageUri: Uri? = null
    private val vm: PrescriptionViewModel by viewModels()

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                // Take persistable URI permission to access the image later
                contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                
                // Copy the image to app's cache directory for reliable access
                val copiedUri = copyImageToCache(uri)
                imageUri = copiedUri ?: uri // Use copied URI if successful, otherwise use original
                
                Log.d("PrescriptionUpload", "Image picked uri=$imageUri")
                Glide.with(this).load(imageUri).into(binding.previewImage)
                binding.previewCard.visibility = android.view.View.VISIBLE
                binding.placeholderText.visibility = android.view.View.GONE
                // Enable upload button when image is selected
                binding.uploadButton.isEnabled = true
            } catch (e: SecurityException) {
                Log.e("PrescriptionUpload", "SecurityException: ${e.message}")
                // If we can't take permission, try to use the URI directly
                imageUri = uri
                Glide.with(this).load(uri).into(binding.previewImage)
                binding.previewCard.visibility = android.view.View.VISIBLE
                binding.placeholderText.visibility = android.view.View.GONE
                binding.uploadButton.isEnabled = true
            } catch (e: Exception) {
                Log.e("PrescriptionUpload", "Error handling image: ${e.message}")
                Toast.makeText(this, "Error loading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun copyImageToCache(sourceUri: Uri): Uri? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(sourceUri)
            if (inputStream == null) {
                Log.e("PrescriptionUpload", "Cannot open input stream for $sourceUri")
                return null
            }
            
            val tempFile = File.createTempFile("prescription_", ".jpg", cacheDir)
            val outputStream = FileOutputStream(tempFile)
            
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            
            Log.d("PrescriptionUpload", "Image copied to cache: ${tempFile.absolutePath}")
            FileProvider.getUriForFile(this, "${packageName}.fileprovider", tempFile)
        } catch (e: Exception) {
            Log.e("PrescriptionUpload", "Error copying image: ${e.message}")
            null
        }
    }

    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imageUri != null) {
            Log.d("PrescriptionUpload", "Photo taken uri=$imageUri")
            Glide.with(this).load(imageUri).into(binding.previewImage)
            binding.previewCard.visibility = android.view.View.VISIBLE
            binding.placeholderText.visibility = android.view.View.GONE
            // Enable upload button when image is selected
            binding.uploadButton.isEnabled = true
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, create temp file and launch camera
            val tempFile = File.createTempFile("prescription_", ".jpg", cacheDir)
            val uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                tempFile
            )
            imageUri = uri
            takePhoto.launch(uri)
        } else {
            Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
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
        
        binding.takePhotoButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted, create temp file and launch camera
                    val tempFile = File.createTempFile("prescription_", ".jpg", cacheDir)
                    val uri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.fileprovider",
                        tempFile
                    )
                    imageUri = uri
                    takePhoto.launch(uri)
                }
                else -> {
                    // Request permission
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }
        
        // Enable/disable upload button based on image selection
        binding.uploadButton.isEnabled = false
        
        binding.uploadButton.setOnClickListener {
            val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Log.e("PrescriptionUpload", "No authenticated user; aborting upload")
                Toast.makeText(this, "Please login to upload", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (imageUri == null) {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val uid = user.uid
            val note = binding.noteInput.text?.toString().orEmpty()
            val path = imageUri!!.toString()
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

