package com.saim.AuthenticationModule

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.saim.AdminModule.AuthViewModel
import com.saim.curify.MainActivity
import com.saim.curify.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var progressDialog: ProgressDialog

    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            binding=ActivitySignupBinding.inflate(layoutInflater)
            setContentView(binding.root)


            viewModel= AuthViewModel()

            viewModel.checkcurrentuser()


            progressDialog= ProgressDialog(this)
            progressDialog.setMessage("Please wait")
            progressDialog.setCancelable(false)
            lifecycleScope.launch {
                viewModel.failureMessage.collect {
                    progressDialog.dismiss()
                    if (it != null) {

                        Toast.makeText(this@SignupActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            lifecycleScope.launch {
                viewModel.currentUser.collect {
                    progressDialog.dismiss()
                    if (it != null) {

                        startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginbtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.backbtn.setOnClickListener {

            finish()
        }
        binding.signbtn.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.Password.text.toString()



            if (name.trim().isEmpty()) {
                Toast.makeText(this, "Name Cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!email.contains("@")) {
                Toast.makeText(this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!binding.checkBox.isChecked) {
                Toast.makeText(this, "You must agree to our conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be atleast 6 words", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressDialog.show()

            viewModel.Signup(email,password,name)
        }
    }
}