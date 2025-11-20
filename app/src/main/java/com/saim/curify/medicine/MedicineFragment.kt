package com.saim.curify.medicine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saim.adapters.GenericListAdapter
import android.content.Intent
import com.saim.curify.R
import com.saim.curify.medicine.prescription.UploadPrescriptionActivity
import com.saim.curify.databinding.ItemMedicineBinding
import com.saim.curify.databinding.FragmentMedicineBinding
import com.saim.domain.entities.Drugs
import kotlinx.coroutines.launch
import java.util.Locale


class MedicineFragment : Fragment() {

    lateinit var binding: FragmentMedicineBinding
    private lateinit var adapter: GenericListAdapter<Drugs, ItemMedicineBinding>
    lateinit var viewModel: MedicineFragmentViewModel
    private val medicineList = ArrayList<Drugs>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GenericListAdapter(
            inflate = { parent -> ItemMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false) },
            bind = { b, medicine: Drugs ->
                b.title.text = medicine.title
                b.weight.text = medicine.weight
                b.status.text = medicine.status
                b.price.text = medicine.price.toString()
                com.bumptech.glide.Glide.with(b.root.context)
                    .load(medicine.image)
                    .error(R.drawable.logo_curify)
                    .placeholder(R.drawable.logo_curify)
                    .into(b.productImage)
                b.root.setOnClickListener {
                    b.root.context.startActivity(
                        android.content.Intent(
                            b.root.context,
                            com.saim.MedicalStoreModule.MedicineDetailActivity::class.java
                        ).putExtra("data", com.google.gson.Gson().toJson(medicine))
                    )
                }
            },
            diff = GenericListAdapter.simpleDiff(
                areItemsTheSame = { o, n -> o.id == n.id },
                areContentsTheSame = { o, n -> o == n }
            )
        )
binding.name.clearFocus()

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Upload prescription button
        binding.loginbtn.setOnClickListener {
            startActivity(Intent(requireContext(), UploadPrescriptionActivity::class.java))
        }

        viewModel = MedicineFragmentViewModel()
        lifecycleScope.launch {
            viewModel.failuremessge.collect {
                it?.let {

                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

                }
            }
        }
        lifecycleScope.launch {
            viewModel.data.collect { data ->
                data?.let {
                    medicineList.clear()
                    medicineList.addAll(it.take(10))
                    adapter.submitList(ArrayList(medicineList))
                }
            }
        }
        binding.name.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        if (query.isNullOrBlank()) {
            // If search is cleared, show full list
            adapter.submitList(ArrayList(medicineList))
            return
        }

        val filteredList = ArrayList<Drugs>()
        for (medicine in medicineList) {
            if (medicine.title?.lowercase(Locale.ROOT)?.contains(query.lowercase()) == true) {
                filteredList.add(medicine)
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(context, "No medicine found", Toast.LENGTH_SHORT).show()
        }

        adapter.submitList(filteredList)
    }

}


