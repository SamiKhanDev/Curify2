package com.saim.curify.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.databinding.ActivityForgotPasswordBinding

/**
 * Forgot Password Activity
 * Handles password reset flow
 * TODO: Integrate with password reset API
 * TODO: Navigate to OTP verification after email submission
 */
class ForgotPasswordActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityForgotPasswordBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
    }
    
    private fun setupViews() {
        binding.resetButton.setOnClickListener {
            // TODO: Validate email
            // TODO: Call password reset API
            // TODO: Navigate to OTP verification
            val intent = Intent(this, OtpVerificationActivity::class.java)
            intent.putExtra("email", binding.emailInput.text.toString())
            startActivity(intent)
        }
        
        binding.backToLogin.setOnClickListener {
            finish()
        }
    }
}

