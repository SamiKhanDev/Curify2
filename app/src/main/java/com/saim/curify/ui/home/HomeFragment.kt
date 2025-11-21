package com.saim.curify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.saim.curify.databinding.FragmentHomeBinding

/**
 * Home Fragment
 * Displays categories, popular medicines, and recommended medicines
 * TODO: Integrate with home data API
 * TODO: Add skeleton loaders for loading states
 */
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerViews()
        setupClickListeners()
        observeViewModel()
        
        // Load data
        viewModel.loadHomeData()
    }
    
    private fun setupRecyclerViews() {
        // Categories RecyclerView
        binding.categoriesRecyclerView.layoutManager = 
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // TODO: Set adapter
        
        // Popular Medicines RecyclerView
        binding.popularMedicinesRecyclerView.layoutManager = 
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // TODO: Set adapter
        
        // Recommended Medicines RecyclerView
        binding.recommendedMedicinesRecyclerView.layoutManager = 
            LinearLayoutManager(requireContext())
        // TODO: Set adapter
    }
    
    private fun setupClickListeners() {
        binding.searchInput.setOnClickListener {
            // TODO: Navigate to search screen
        }
        
        binding.seeAllPopular.setOnClickListener {
            // TODO: Navigate to medicine list
        }
        
        binding.buyMedicineFab.setOnClickListener {
            // TODO: Navigate to medicine list
        }
    }
    
    private fun observeViewModel() {
        viewModel.greeting.observe(viewLifecycleOwner) { greeting ->
            binding.greetingText.text = greeting
        }
        
        // TODO: Observe other LiveData from ViewModel
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

