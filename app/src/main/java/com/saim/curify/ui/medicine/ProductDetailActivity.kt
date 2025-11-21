package com.saim.curify.ui.medicine

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.databinding.ScreenProductDetailBinding
import com.saim.curify.ui.cart.CartFragment

/**
 * Product Detail Activity
 * Displays detailed information about a medicine
 * TODO: Integrate with medicine detail API
 * TODO: Add image gallery
 */
class ProductDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ScreenProductDetailBinding
    private var quantity = 1
    private var medicineId: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScreenProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        medicineId = intent.getStringExtra("medicine_id")
        setupViews()
        loadMedicineDetails()
    }
    
    private fun setupViews() {
        binding.increaseButton.setOnClickListener {
            quantity++
            binding.quantityText.text = quantity.toString()
        }
        
        binding.decreaseButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.quantityText.text = quantity.toString()
            }
        }
        
        binding.addToCartButton.setOnClickListener {
            // TODO: Add to cart
            // TODO: Show snackbar confirmation
        }
        
        binding.buyNowButton.setOnClickListener {
            // TODO: Add to cart and navigate to checkout
            val intent = Intent(this, com.saim.curify.ui.checkout.CheckoutActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun loadMedicineDetails() {
        // TODO: Load medicine details from API
        // Mock data for now
        binding.productTitle.text = "Medicine Name"
        binding.weight.text = "500mg"
        binding.description.text = "Medicine description goes here..."
        binding.price.text = "999"
    }
}

