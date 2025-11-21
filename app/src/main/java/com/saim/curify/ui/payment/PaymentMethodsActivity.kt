package com.saim.curify.ui.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.databinding.ActivityPaymentMethodsBinding

/**
 * Payment Methods Activity
 * Allows user to select payment method
 * TODO: Integrate JazzCash SDK
 * TODO: Integrate EasyPaisa SDK
 * TODO: Integrate Card payment gateway
 */
class PaymentMethodsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPaymentMethodsBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.jazzcashCard.setOnClickListener {
            // TODO: Navigate to JazzCash payment details
            navigateToPaymentDetails("JazzCash")
        }
        
        binding.easypaisaCard.setOnClickListener {
            // TODO: Navigate to EasyPaisa payment details
            navigateToPaymentDetails("EasyPaisa")
        }
        
        binding.cardCard.setOnClickListener {
            // TODO: Integrate card payment gateway
            // TODO: Navigate to card payment screen
            setResult(RESULT_OK, Intent().putExtra("payment_method", "Card"))
            finish()
        }
        
        binding.codCard.setOnClickListener {
            // Cash on Delivery - no additional details needed
            setResult(RESULT_OK, Intent().putExtra("payment_method", "Cash on Delivery"))
            finish()
        }
    }
    
    private fun navigateToPaymentDetails(paymentMethod: String) {
        val intent = Intent(this, PaymentDetailsActivity::class.java)
        intent.putExtra("payment_method", paymentMethod)
        startActivityForResult(intent, REQUEST_PAYMENT_DETAILS)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PAYMENT_DETAILS && resultCode == RESULT_OK) {
            val paymentMethod = data?.getStringExtra("payment_method")
            setResult(RESULT_OK, Intent().putExtra("payment_method", paymentMethod))
            finish()
        }
    }
    
    companion object {
        private const val REQUEST_PAYMENT_DETAILS = 1002
    }
}

