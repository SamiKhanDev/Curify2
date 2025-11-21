package com.saim.curify.ui.cart

import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        
        // Get status bar height directly
        val statusBarHeight = getStatusBarHeight()
        val padding = resources.getDimensionPixelSize(com.saim.curify.R.dimen.spacing_medium)
        
        // Apply padding to NestedScrollView immediately to push content below status bar
        binding.nestedScrollView.setPadding(
            padding,
            padding + statusBarHeight,
            padding,
            padding
        )
        
        // Also set up insets listener as backup
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val currentPadding = resources.getDimensionPixelSize(com.saim.curify.R.dimen.spacing_medium)
            
            // Update padding if insets are available
            binding.nestedScrollView.setPadding(
                currentPadding,
                currentPadding + systemBars.top,
                currentPadding,
                currentPadding
            )
            
            insets
        }
        
        // Request insets to be applied
        ViewCompat.requestApplyInsets(binding.root)
        
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        
        viewModel.loadCart()
    }
    
    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        // Fallback: use a reasonable default if resource not found
        if (result == 0) {
            result = (24 * resources.displayMetrics.density).toInt() // 24dp default
        }
        return result
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

