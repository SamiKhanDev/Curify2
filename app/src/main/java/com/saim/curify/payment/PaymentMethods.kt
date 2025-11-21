package com.saim.curify.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.saim.curify.R
import com.saim.curify.databinding.ActivityPaymentMethodsBinding
import kotlinx.coroutines.launch

class PaymentMethods : AppCompatActivity() {
    lateinit var binding: ActivityPaymentMethodsBinding
    lateinit var viewModel: PaymentViewModel
    companion object {
        var jazzcash = false
        var easypaisa = false
        var nameq = ""
        var selectedImageResId: Int = R.drawable.jazzcash
        var numberq=""


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityPaymentMethodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Apply system window insets to avoid status bar overlap
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val padding = resources.getDimensionPixelSize(com.saim.curify.R.dimen.spacing_medium)
            v.setPadding(padding, padding + systemBars.top, padding, padding)
            insets
        }

        viewModel = PaymentViewModel()

        lifecycleScope.launch {
            viewModel.isSuccessfullySaved.collect{
                it?.let {
                    if(it)
                        Toast.makeText(this@PaymentMethods, "Account Added Successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.failureMessage.collect{
                it?.let {

                        Toast.makeText(this@PaymentMethods, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

      binding.jazzcashCard.setOnClickListener{
    startActivity(Intent(this@PaymentMethods, JazzcashPaymentDetails::class.java))
          finish()


}
        binding.easypaisaCard.setOnClickListener{
    startActivity(Intent(this@PaymentMethods, EasyPaisaPaymentDetails::class.java))
            finish()


}



    }
}