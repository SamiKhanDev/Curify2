package com.saim.curify.ui.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.R
import com.saim.curify.databinding.ActivityPaymentDetailsBinding

/**
 * Payment Details Activity
 * Collects payment method details (JazzCash/EasyPaisa)
 * TODO: Integrate with JazzCash SDK
 * TODO: Integrate with EasyPaisa SDK
 * TODO: Add validation
 */
class PaymentDetailsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPaymentDetailsBinding
    private var paymentMethod: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        paymentMethod = intent.getStringExtra("payment_method")
        setupViews()
    }
    
    private fun setupViews() {
        when (paymentMethod) {
            "JazzCash" -> {
                binding.title.text = getString(R.string.enter_jazzcash_details)
                binding.paymentMethodIcon.setImageResource(R.drawable.jazzcash)
            }
            "EasyPaisa" -> {
                binding.title.text = getString(R.string.enter_easypaisa_details)
                binding.paymentMethodIcon.setImageResource(R.drawable.easypaisa)
            }
        }
        
        binding.addAccountButton.setOnClickListener {
            // TODO: Validate inputs
            // TODO: Save payment method
            // TODO: Integrate with payment SDK
            val accountName = binding.accountHolderNameInput.text.toString()
            val accountNumber = binding.accountHolderNumberInput.text.toString()
            
            // For now, just return success
            setResult(RESULT_OK, Intent().putExtra("payment_method", paymentMethod))
            finish()
        }
    }
}

