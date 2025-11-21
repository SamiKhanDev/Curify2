package com.saim.curify.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saim.AuthenticationModule.LoginActivity
import com.saim.curify.payment.PaymentDetailActivity
import com.saim.curify.payment.PaymentMethods
import com.saim.AdminModule.AuthViewModel
import com.saim.curify.MainActivity
import com.saim.curify.MyCart
import com.saim.curify.R
import com.saim.curify.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: AuthViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name.text= MainActivity.names
        viewModel = AuthViewModel()

binding.paymentmethodbtn.setOnClickListener{
    if(PaymentMethods.easypaisa || PaymentMethods.jazzcash) {
        startActivity(Intent(context, PaymentDetailActivity::class.java))
    }
    else {
        startActivity(Intent(context, PaymentMethods::class.java))
    }

}
        binding.mycartbtn.setOnClickListener{
            startActivity(Intent(context, MyCart::class.java))
        }

        binding.prescriptionsBtn.setOnClickListener {
            findNavController().navigate(R.id.item_prescriptions)
        }

        binding.checkappointmentbtn.setOnClickListener {
            findNavController().navigate(R.id.item_appointments)
        }

        binding.logoutbtn.setOnClickListener{

            viewModel.logout(true)


        }
        lifecycleScope.launch {
            viewModel.islogoutsuccess.collect {
                it?.let {
                    if (it) {
                        Toast.makeText(context, "logged out", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(context, LoginActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }
    }
}