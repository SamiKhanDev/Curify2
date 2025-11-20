package com.saim.AdminModule

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.databinding.ActivityAdminManageMedicineBinding

class AdminManageMedicine : AppCompatActivity() {
    lateinit var binding: ActivityAdminManageMedicineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminManageMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.addmed.setOnClickListener{

    startActivity(Intent(this, AdminUADMedicine::class.java))

}

    }
}