package com.saim.AdminModule

import AdminAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.saim.DataSources.CloudinaryUploadHelper.Companion.initializeCloudinary
import com.saim.MedicalStoreModule.AddMedicine
import com.saim.MedicalStoreModule.MedicineViewModel
import com.saim.MedicalStoreModule.UpdateMedicine
import com.saim.curify.databinding.ActivityAdminUadmedicineBinding
import com.saim.curify.medicine.MedicineFragmentViewModel
import com.saim.domain.entities.Drugs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminUADMedicine : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUadmedicineBinding
    private lateinit var adapter: AdminAdapter
    private val viewModelp: MedicineViewModel by viewModels()
    private lateinit var viewModel: MedicineFragmentViewModel
    private var items = ArrayList<Drugs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout
        binding = ActivityAdminUadmedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔥 Step 1: Initialize Cloudinary (REQUIRED)
        initializeCloudinary(this)

        // Initialize adapter
        adapter = AdminAdapter(
            items,
            onDeleteClick = { medicine ->
                viewModelp.deletePetShop(medicine.id)
            },
            onUpdateClick = { medicine ->
                startActivity(
                    Intent(this, UpdateMedicine::class.java)
                        .putExtra("data", Gson().toJson(medicine))
                )
            }
        )


        binding.addmed.setOnClickListener {
            // Open AddMedicine screen (this is where image uploading happens)
            startActivity(Intent(this, AddMedicine::class.java))
        }

        // RecyclerView setup
        binding.adminrecyclerview.adapter = adapter
        binding.adminrecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // ViewModel for medicines
        viewModel = MedicineFragmentViewModel()

        // Observe error messages
        lifecycleScope.launch {
            viewModel.failuremessge.collect {
                it?.let { msg ->
                    Toast.makeText(this@AdminUADMedicine, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observe medicine list data
        lifecycleScope.launch {
            viewModel.data.collect { list ->
                list?.let {
                    items.clear()
                    items.addAll(0, it)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}
