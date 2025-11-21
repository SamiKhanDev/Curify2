package com.saim.curify.ui.checkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.databinding.ActivityCheckoutBinding
import com.saim.curify.ui.payment.PaymentMethodsActivity

/**
 * Checkout Activity
 * Handles order placement
 * TODO: Integrate with order API
 * TODO: Add address selection
 */
class CheckoutActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCheckoutBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
    }
    
    private fun setupViews() {
        binding.paymentMethodCard.setOnClickListener {
            val intent = Intent(this, PaymentMethodsActivity::class.java)
            startActivityForResult(intent, REQUEST_PAYMENT_METHOD)
        }
        
        binding.placeOrderButton.setOnClickListener {
            // TODO: Validate payment method
            // TODO: Create order
            // TODO: Navigate to order confirmation
            finish()
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PAYMENT_METHOD && resultCode == RESULT_OK) {
            // TODO: Update payment method display
            val paymentMethod = data?.getStringExtra("payment_method")
            binding.paymentMethodText.text = paymentMethod ?: "Select Payment Method"
        }
    }
    
    companion object {
        private const val REQUEST_PAYMENT_METHOD = 1001
    }
}

