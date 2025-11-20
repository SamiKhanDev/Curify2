package com.saim.MedicalStoreModule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.saim.MedicalStoreModule.UpdateMedicine.Companion.medid
import com.saim.domain.entities.Drugs
import com.saim.domain.entities.MyCartData
import com.saim.curify.databinding.ActivityMedicineDetailBinding
import com.google.gson.Gson

@AndroidEntryPoint
class MedicineDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityMedicineDetailBinding
    lateinit var medicine: Drugs
    private var count = 1
    private val viewModel: MyCartViewModel by viewModels()
    private var price = 0
    private var addprice =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewModel provided by Hilt
        binding = ActivityMedicineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicine = Gson().fromJson(intent.getStringExtra("data"), Drugs::class.java)
        medid = medicine.id

        binding.medname.setText(medicine.title)
        binding.meddesc.setText(medicine.description)
        binding.medprice.setText("PKR "+medicine.price)
        price=medicine.price.toIntOrNull()!!
        addprice=medicine.price.toIntOrNull()!!
        binding.medweight.setText(medicine.weight)

        binding.plus.setOnClickListener {
            if(count < medicine.quantity.toIntOrNull()!!) {
                count++
price=price+addprice
                binding.inputquantity.text = count.toString()
                binding.medprice.setText("PKR "+price)
            }
        }

        binding.minus.setOnClickListener {
            if (count > 1) {
                count--
                price=price-addprice
                binding.inputquantity.text = count.toString()
                binding.medprice.setText("PKR "+price)
            }

        }
        binding.addtocartbtn.setOnClickListener{
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