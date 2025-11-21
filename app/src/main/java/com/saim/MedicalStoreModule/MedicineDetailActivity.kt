package com.saim.MedicalStoreModule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.saim.MedicalStoreModule.UpdateMedicine.Companion.medid
import com.saim.domain.entities.Drugs
import com.saim.domain.entities.MyCartData
import com.saim.curify.databinding.ScreenProductDetailBinding
import com.google.gson.Gson

@AndroidEntryPoint
class MedicineDetailActivity : AppCompatActivity() {
    lateinit var binding: ScreenProductDetailBinding
    lateinit var medicine: Drugs
    private var count = 1
    private val viewModel: MyCartViewModel by viewModels()
    private var price = 0
    private var addprice =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewModel provided by Hilt
        binding = ScreenProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicine = Gson().fromJson(intent.getStringExtra("data"), Drugs::class.java)
        medid = medicine.id

        binding.productTitle.text = medicine.title
        binding.description.text = medicine.description
        binding.price.text = medicine.price.toString()
        binding.currency.text = "PKR "
        price = medicine.price.toIntOrNull() ?: 0
        addprice = medicine.price.toIntOrNull() ?: 0
        binding.weight.text = medicine.weight
        
        // Load medicine image using Glide
        com.bumptech.glide.Glide.with(this)
            .load(medicine.image)
            .error(com.saim.curify.R.drawable.logo_curify)
            .placeholder(com.saim.curify.R.drawable.logo_curify)
            .into(binding.productImage)

        binding.increaseButton.setOnClickListener {
            if(count < (medicine.quantity.toIntOrNull() ?: 0)) {
                count++
                price = price + addprice
                binding.quantityText.text = count.toString()
                binding.price.text = price.toString()
            }
        }

        binding.decreaseButton.setOnClickListener {
            if (count > 1) {
                count--
                price = price - addprice
                binding.quantityText.text = count.toString()
                binding.price.text = price.toString()
            }
        }
        
        binding.addToCartButton.setOnClickListener {
            val mycart = MyCartData()
            mycart.title = medicine.title
            mycart.price = medicine.price
            mycart.quantity = count.toString()
            mycart.weight = medicine.weight
            mycart.image = medicine.image
            val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
            viewModel.savemycart(uid, mycart)
        }
    }
}