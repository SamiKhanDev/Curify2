package com.saim.MedicalStoreModule

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.saim.DataSources.CloudinaryUploadHelper
import com.saim.DataSources.CloudinaryUploadHelper.Companion.initializeCloudinary
import com.saim.curify.databinding.ActivityAddMedicineBinding
import com.saim.domain.entities.Drugs

@AndroidEntryPoint
class AddMedicine : AppCompatActivity() {

    private lateinit var binding: ActivityAddMedicineBinding

    // Hilt ViewModel injection
    private val viewModel: MedicineViewModel by viewModels()

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Cloudinary
        initializeCloudinary(this)

        binding.medicon.setOnClickListener {
            chooseImageFromGallery()
        }

        binding.addmed.setOnClickListener {
            saveMedicine()
        }
    }

    // ---------------- IMAGE PICKER ----------------
    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            if (selectedImageUri != null) {
                binding.medicon.setImageURI(selectedImageUri)
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------------- SAVE MEDICINE ----------------
    private fun saveMedicine() {

        val title = binding.medname.text.toString().trim()
        val desc = binding.meddesc.text.toString().trim()
        val weight = binding.medweight.text.toString().trim()
        val quantity = binding.medquantity.text.toString().trim()
        val price = binding.medprice.text.toString().trim()

        val selectedRadioId = binding.medicineTypeRadioGroup.checkedRadioButtonId
        if (selectedRadioId == -1) {
            Toast.makeText(this, "Select medicine type", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedType = binding.root.findViewById<RadioButton>(selectedRadioId).text.toString()

        if (title.isEmpty() || desc.isEmpty() || weight.isEmpty() ||
            quantity.isEmpty() || price.isEmpty()
        ) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val medicine = Drugs().apply {
            this.title = title
            this.description = desc
            this.weight = weight
            this.quantity = quantity
            this.price = price
            this.status = "In Stock"
            this.type = selectedType
        }

        if (selectedImageUri == null) {
            viewModel.savemedicine(medicine)
            Toast.makeText(this, "Medicine Saved", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        uploadImageToCloudinary(medicine)
    }

    // ---------------- CLOUDINARY ----------------
    private fun uploadImageToCloudinary(medicine: Drugs) {

        val uriString = selectedImageUri!!.toString()
        val uploader = CloudinaryUploadHelper()

        Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show()

        uploader.uploadFile(uriString) { success, imageUrl ->

            if (!success) {
                Toast.makeText(this, "Upload Failed: $imageUrl", Toast.LENGTH_LONG).show()
                return@uploadFile
            }

            medicine.image = imageUrl

            viewModel.savemedicine(medicine)

            Toast.makeText(this, "Medicine Added Successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
