package com.saim.curify.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.databinding.ActivityLoginBinding
import com.saim.curify.ui.MainActivity

/**
 * Login Activity
 * Handles user authentication
 * TODO: Integrate with authentication service/API
 */
class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
    }
    
    private fun setupViews() {
        binding.loginButton.setOnClickListener {
            // TODO: Validate inputs
            // TODO: Call authentication API
            // TODO: Handle success/error responses
            // For now, navigate to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        
        binding.signupLink.setOnClickListener {
            // TODO: Navigate to SignupActivity
        }
        
        binding.forgotPassword.setOnClickListener {
            // TODO: Navigate to ForgotPasswordActivity
        }
    }
}

