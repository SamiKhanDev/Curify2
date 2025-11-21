package com.saim.curify.ui.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.saim.curify.databinding.FragmentMedicineListBinding

/**
 * Medicine List Fragment
 * Displays list of medicines with search functionality
 * TODO: Integrate with medicine API
 * TODO: Add pagination
 * TODO: Add filters
 */
class MedicineListFragment : Fragment() {
    
    private var _binding: FragmentMedicineListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MedicineListViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicineListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupSearch()
        observeViewModel()
        
        viewModel.loadMedicines()
    }
    
    private fun setupRecyclerView() {
        binding.medicinesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        // TODO: Set adapter
    }
    
    private fun setupSearch() {
        binding.searchInput.setOnEditorActionListener { _, _, _ ->
            // TODO: Perform search
            viewModel.searchMedicines(binding.searchInput.text.toString())
            true
        }
    }
    
    private fun observeViewModel() {
        viewModel.medicines.observe(viewLifecycleOwner) { medicines ->
            if (medicines.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
            } else {
                binding.emptyState.visibility = View.GONE
                // TODO: Update adapter
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

