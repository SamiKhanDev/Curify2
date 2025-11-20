package com.saim.AuthenticationModule

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.saim.AdminModule.AuthViewModel
import com.saim.curify.databinding.ActivityForgetpasswordBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class Forgetpassword : AppCompatActivity() {
    lateinit var binding: ActivityForgetpasswordBinding
    lateinit var progressDialog: ProgressDialog



    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgetpasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel= AuthViewModel()



        progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.setCancelable(false)
        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                progressDialog.dismiss()
                if (it != null) {

                    Toast.makeText(this@Forgetpassword, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.resetresponse.collect {
                progressDialog.dismiss()
                if (it != null) {

                    val builder= MaterialAlertDialogBuilder(this@Forgetpassword)
                    builder.setMessage("We have sent you an email")
                    builder.setCancelable(false)
                    builder.setPositiveButton("ok", DialogInterface.OnClickListener{ dialogInterface, i ->
                        finish()

                    })
                    builder.show()
                }
            }
        }

        binding.signupTxt.setOnClickListener{

            finish()
        }
        binding.resetpassbtn.setOnClickListener {
            val email = binding.email.text.toString()


            if (!email.contains("@")) {
                Toast.makeText(this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            progressDialog.show()

            viewModel.resetpassword(email)
        }



    }
}