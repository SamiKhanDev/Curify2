package com.saim.curify.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.R
import com.saim.curify.databinding.ActivityPaymentDetailBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class PaymentDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentDetailBinding
    lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityPaymentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = PaymentViewModel()
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
        viewModel.readpetshop(uid)

        lifecycleScope.launch {
            viewModel.data.collect { pm ->
                pm?.let {
                    binding.name.text = it.name
                    binding.number.text = it.phonenumber
                    if (it.Method == "Jazzcash") {
                        binding.pymticon.setImageResource(R.drawable.jazzcash)
                    } else if (it.Method == "EasyPaisa") {
                        binding.pymticon.setImageResource(R.drawable.easypaisa)
                    }
                }
            }
        }
        binding.changepymtbtn.setOnClickListener{
            startActivity(Intent(this@PaymentDetailActivity, PaymentMethods::class.java))
            finish()
        }


    }
}