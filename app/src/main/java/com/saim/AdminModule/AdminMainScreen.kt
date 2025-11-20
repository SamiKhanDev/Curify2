package com.saim.AdminModule

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.AuthenticationModule.LoginActivity
import com.saim.curify.databinding.ActivityAdminMainScreenBinding

class AdminMainScreen : AppCompatActivity() {
    lateinit var binding: ActivityAdminMainScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.logoutad.setOnClickListener{
    startActivity(Intent(this@AdminMainScreen, LoginActivity::class.java))
    finish()

}
        binding.managemed.setOnClickListener{
            startActivity(Intent(this@AdminMainScreen, AdminManageMedicine::class.java))

        }
        binding.managedoc.setOnClickListener {
            startActivity(Intent(this@AdminMainScreen, AdminManageDoctors::class.java))
        }

        binding.manageorder.setOnClickListener {
            startActivity(Intent(this@AdminMainScreen, AdminManageOrders::class.java))
        }

    }
}