package com.saim.curify.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saim.curify.databinding.ActivityOtpVerificationBinding
import com.saim.curify.ui.MainActivity

/**
 * OTP Verification Activity
 * Handles OTP verification for password reset and signup
 * TODO: Integrate with OTP verification service
 * TODO: Add SMS Retriever API for auto-fill
 * TODO: Add countdown timer for resend OTP
 */
class OtpVerificationActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityOtpVerificationBinding
    private var email: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        email = intent.getStringExtra("email")
        setupViews()
    }
    
    private fun setupViews() {
        email?.let {
            binding.subtitle.text = getString(com.saim.curify.R.string.otp_sent_to, it)
        }
        
        binding.verifyButton.setOnClickListener {
            // TODO: Validate OTP
            // TODO: Call OTP verification API
            // TODO: Handle success/error
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        
        binding.resendOtp.setOnClickListener {
            // TODO: Resend OTP
            // TODO: Start countdown timer
        }
    }
}

