package com.saim.curify.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.R
import com.saim.curify.databinding.ActivityJazzcashPaymentDetailsBinding
import com.saim.domain.entities.PaymentTypeData

class JazzcashPaymentDetails : AppCompatActivity() {
    lateinit var binding: ActivityJazzcashPaymentDetailsBinding
    lateinit var viewModel: PaymentViewModel
    companion object {
        var paymentmethodadded=false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = PaymentViewModel()
        binding= ActivityJazzcashPaymentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addbtnjc.setOnClickListener{
            val name = binding.holdernamejc.text.toString()
            val phone = binding.holderphonenumberjc.text.toString()
            if (!name.matches(Regex("^[a-zA-Z ]+$"))) {
                Toast.makeText(this, "Name must contain only letters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (phone.isEmpty() || !phone.startsWith("92") || phone.length < 12) {
                Toast.makeText(this, "Phone number must start with 92 and be valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val paymenttype = PaymentTypeData()
            paymenttype.name = name
            paymenttype.Method="Jazzcash"
            paymenttype.phonenumber = phone
            paymenttype.timestamp = System.currentTimeMillis()
            paymenttype.uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
            viewModel.savepaymenttype(paymenttype)

            Toast.makeText(this@JazzcashPaymentDetails, "Account Added Successfully", Toast.LENGTH_SHORT).show()
            paymentmethodadded =true
PaymentMethods.jazzcash =true
            PaymentMethods.easypaisa =false
            PaymentMethods.nameq =name
            PaymentMethods.numberq =phone
            PaymentMethods.selectedImageResId = R.drawable.jazzcash
            startActivity(Intent(this@JazzcashPaymentDetails, PaymentDetailActivity::class.java))


            finish()
        }

        }
    }
