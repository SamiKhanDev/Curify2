package com.saim.curify.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.saim.curify.databinding.FragmentCartBinding
import com.saim.curify.ui.checkout.CheckoutActivity

/**
 * Cart Fragment
 * Displays cart items and total
 * TODO: Integrate with cart repository
 */
class CartFragment : Fragment() {
    
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        
        viewModel.loadCart()
    }
    
    private fun setupRecyclerView() {
        binding.cartItemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        // TODO: Set adapter
    }
    
    private fun setupClickListeners() {
        binding.checkoutButton.setOnClickListener {
            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun observeViewModel() {
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
            } else {
                binding.emptyState.visibility = View.GONE
                // TODO: Update adapter
            }
        }
        
        viewModel.subtotal.observe(viewLifecycleOwner) { subtotal ->
            binding.subtotalText.text = "PKR $subtotal"
        }
        
        viewModel.taxes.observe(viewLifecycleOwner) { taxes ->
            binding.taxesText.text = "PKR $taxes"
        }
        
        viewModel.total.observe(viewLifecycleOwner) { total ->
            binding.totalText.text = "PKR $total"
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

