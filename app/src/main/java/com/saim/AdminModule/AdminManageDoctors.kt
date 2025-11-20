package com.saim.AdminModule

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.saim.DataSources.CloudinaryUploadHelper
import com.saim.curify.databinding.ActivityAdminManageDoctorsBinding

class AdminManageDoctors : AppCompatActivity() {

    private lateinit var binding: ActivityAdminManageDoctorsBinding
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1001

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminManageDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Manage Doctors"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        CloudinaryUploadHelper.initializeCloudinary(this)

        binding.btnSelectImage.setOnClickListener { pickImage() }

        binding.btnAddDoctor.setOnClickListener { saveDoctor() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    // -------------------- PICK IMAGE --------------------

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgDoctor.setImageURI(selectedImageUri)
        }
    }

    // -------------------- SAVE DOCTOR --------------------

    private fun saveDoctor() {

        val name = binding.etName.text.toString().trim()
        val age = binding.etAge.text.toString().trim()
        val gender = binding.etGender.text.toString().trim()
        val qualification = binding.etQualification.text.toString().trim()
        val specialization = binding.etSpecialization.text.toString().trim()
        val startTime = binding.etStartTime.text.toString().trim()
        val endTime = binding.etEndTime.text.toString().trim()
        val hospital = binding.etHospital.text.toString().trim()
        val fee = binding.etFee.text.toString().trim()

        // VALIDATION
        if (selectedImageUri == null) {
            Toast.makeText(this, "Select Doctor Photo", Toast.LENGTH_SHORT).show()
            return
        }

        if (name.isEmpty() || age.isEmpty() || gender.isEmpty() ||
            qualification.isEmpty() || specialization.isEmpty() ||
            startTime.isEmpty() || endTime.isEmpty() ||
            hospital.isEmpty() || fee.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = ProgressDialog(this)
        dialog.setMessage("Uploading...")
        dialog.setCancelable(false)
        dialog.show()

        // UPLOAD IMAGE TO CLOUDINARY
        CloudinaryUploadHelper().uploadFile(selectedImageUri.toString()) { success, url ->

            if (!success || url == null) {
                dialog.dismiss()
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
                return@uploadFile
            }

            // Generate Doctor ID
            val doctorId = db.collection("Doctors").document().id

            // SAVE TO FIRESTORE
            val doctorData = hashMapOf(
                "id" to doctorId,
                "name" to name,
                "age" to age,
                "gender" to gender,
                "qualification" to qualification,
                "specialization" to specialization,
                "photo" to url,
                "startTime" to startTime,
                "endTime" to endTime,
                "status" to "available",
                "hospital" to hospital,
                "fee" to fee + " PKR"
            )

            db.collection("Doctors")
                .document(doctorId)
                .set(doctorData)
                .addOnSuccessListener {
                    dialog.dismiss()
                    Toast.makeText(this, "Doctor Added Successfully!", Toast.LENGTH_LONG).show()
                    clearFields()
                }
                .addOnFailureListener {
                    dialog.dismiss()
                    Toast.makeText(this, "Failed to Save Doctor Data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun clearFields() {
        binding.etName.text.clear()
        binding.etAge.text.clear()
        binding.etGender.text.clear()
        binding.etQualification.text.clear()
        binding.etSpecialization.text.clear()
        binding.etStartTime.text.clear()
        binding.etEndTime.text.clear()
        binding.etHospital.text.clear()
        binding.etFee.text.clear()
        binding.imgDoctor.setImageResource(0)
        selectedImageUri = null
    }
}
