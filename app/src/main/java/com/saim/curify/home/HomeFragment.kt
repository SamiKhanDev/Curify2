package com.saim.curify.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.saim.adapters.GenericListAdapter
import com.saim.curify.MainActivity
import com.saim.curify.medicine.MedicineFragmentViewModel
import com.saim.curify.R
import com.saim.curify.databinding.FragmentHomeBinding
import com.saim.curify.databinding.ItemMedicineBinding
import com.saim.domain.entities.Drugs
import com.saim.domain.entities.MyCartData
import com.saim.MedicalStoreModule.MyCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: GenericListAdapter<Drugs, ItemMedicineBinding>
    private val allMedicines = ArrayList<Drugs>()
    private val cartViewModel: MyCartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Greeting
        binding.greetingText.text = "Hi, ${MainActivity.names.ifBlank { "there" }}"

        // Search input: focus for typing
        binding.searchInput.setOnClickListener {
            binding.searchInput.isFocusableInTouchMode = true
            binding.searchInput.isCursorVisible = true
            binding.searchInput.requestFocus()
        }

        binding.seeAllPopular.setOnClickListener {
            findNavController().navigate(R.id.item_medicine)
        }

        adapter = GenericListAdapter(
            inflate = { parent -> ItemMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false) },
            bind = { b, medicine: Drugs ->
                b.title.text = medicine.title
                b.weight.text = medicine.weight
                b.status.text = medicine.status
                b.price.text = medicine.price.toString()
                com.bumptech.glide.Glide.with(b.root.context)
                    .load(medicine.image)
                    .error(com.saim.curify.R.drawable.logo_curify)
                    .placeholder(com.saim.curify.R.drawable.logo_curify)
                    .into(b.productImage)
                
                // Add to cart button click listener
                b.addButton.setOnClickListener { view ->
                    view.isClickable = false // Prevent multiple clicks
                    val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        Toast.makeText(b.root.context, "Please login to add items to cart", Toast.LENGTH_SHORT).show()
                        view.isClickable = true
                        return@setOnClickListener
                    }
                    
                    // Check if medicine is in stock (case-insensitive check)
                    val status = medicine.status?.trim() ?: ""
                    if (status.equals("In Stock", ignoreCase = true).not()) {
                        Toast.makeText(b.root.context, "Medicine is out of stock", Toast.LENGTH_SHORT).show()
                        view.isClickable = true
                        return@setOnClickListener
                    }
                    
                    val mycart = MyCartData()
                    mycart.title = medicine.title
                    mycart.price = medicine.price
                    mycart.quantity = "1"
                    mycart.weight = medicine.weight
                    mycart.image = medicine.image
                    
                    val uid = user.uid
                    cartViewModel.savemycart(uid, mycart)
                    
                    Snackbar.make(binding.root, "${medicine.title} added to cart", Snackbar.LENGTH_SHORT).show()
                    view.isClickable = true
                }
                
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
        binding.popularMedicinesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.popularMedicinesRecyclerView.adapter = adapter

        // ViewModel and lifecycle-aware collection
        val vm = MedicineFragmentViewModel()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                vm.data.collect { list ->
                    list?.let {
                        allMedicines.clear()
                        allMedicines.addAll(it)
                        // Default view: top 10
                        adapter.submitList(it.take(10))
                        binding.emptyState.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
            }
        }
        
        // Handle cart add success/error
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.isSuccessfullySaved.collect { success ->
                success?.let {
                    if (it) {
                        // Item added successfully (Snackbar already shown in click listener)
                    }
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.failureMessage.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Inline filtering as user types
        binding.searchInput.addTextChangedListener { text ->
            val query = text?.toString()?.trim()?.lowercase().orEmpty()
            if (query.isEmpty()) {
                adapter.submitList(allMedicines.take(10))
            } else {
                val filtered = allMedicines.filter { med ->
                    med.title.lowercase().contains(query)
                }
                adapter.submitList(filtered)
            }
        }
    }
}