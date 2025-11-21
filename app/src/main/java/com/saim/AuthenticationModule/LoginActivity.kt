package com.saim.AuthenticationModule

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.saim.AdminModule.AdminMainScreen
import com.saim.AdminModule.AuthViewModel
import com.saim.curify.MainActivity
import com.saim.curify.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel= AuthViewModel()
        viewModel.checkcurrentuser()
        progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.setCancelable(false)
        lifecycleScope.launch {
            viewModel.failureMessage.collect {

                if (it != null) {
                    progressDialog.dismiss()
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.currentUser.collect {

                if (it != null) {
                    progressDialog.dismiss()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
        binding.signupLink.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, Forgetpassword::class.java))
            finish()
        }

        binding.backbtn.setOnClickListener {
            finish()
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (!email.contains("@")) {
                Toast.makeText(this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.length<6){
                Toast.makeText(this, "Password must be atleast 6 words", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

if (email.equals("curify90@gmail.com")&& password=="66732979") {
    startActivity(Intent(this@LoginActivity, AdminMainScreen::class.java))
    finish()
}
else {
    progressDialog.show()

    viewModel.login(email, password)
}
        }



    }
}