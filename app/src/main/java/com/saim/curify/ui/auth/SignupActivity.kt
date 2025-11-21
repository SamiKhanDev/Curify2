package com.saim.curify.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.databinding.ActivitySignupBinding
import com.saim.curify.ui.MainActivity

/**
 * Signup Activity
 * Handles new user registration
 * TODO: Integrate with registration API
 * TODO: Add email validation
 * TODO: Add password strength validation
 */
class SignupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySignupBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
    }
    
    private fun setupViews() {
        binding.signupButton.setOnClickListener {
            // TODO: Validate inputs
            // TODO: Check terms checkbox
            // TODO: Call registration API
            // TODO: Navigate to OTP verification
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        
        binding.loginLink.setOnClickListener {
            finish()
        }
    }
}

