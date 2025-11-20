package com.saim.AdminModule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.databinding.ActivityAdminManageOrdersBinding

class AdminManageOrders : AppCompatActivity() {

    private lateinit var binding: ActivityAdminManageOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminManageOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Manage Orders"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TODO: Add logic to display all customer orders
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
