package com.saim.AdminModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saim.data.repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel(){
    val authRepository= AuthRepository()

    val currentUser= MutableStateFlow<FirebaseUser?>(null)
    val failureMessage= MutableStateFlow<String?>(null)
    val resetresponse= MutableStateFlow<Boolean?>(null)
    val islogoutsuccess= MutableStateFlow<Boolean?>(null)
    fun login(email:String,password:String){
        viewModelScope.launch {
            val result=authRepository.login(email,password)
            if(result.isSuccess){
                currentUser.value= result.getOrThrow()
            }
            else{
                failureMessage.value=result.exceptionOrNull()?.message
            }

        }

    }
    fun Signup(email:String,password:String,name:String){
        viewModelScope.launch {
            val result=authRepository.Signup(email,password, name )
            if(result.isSuccess){
                currentUser.value= result.getOrThrow()
            }
            else{
                failureMessage.value=result.exceptionOrNull()?.message
            }

        }

    }
    fun checkcurrentuser(){
        currentUser.value=authRepository.getCurrentUser()
    }

    fun resetpassword(email:String){
        viewModelScope.launch {
            val result=authRepository.resetPassword(email )
            if(result.isSuccess){
                resetresponse.value= result.getOrThrow()
            }
            else{
                failureMessage.value=result.exceptionOrNull()?.message
            }

        }

    }

    fun logout(check:Boolean) {
        viewModelScope.launch {
            if (check) {
                val result = authRepository.logout()
                if (result.isSuccess) {
                    islogoutsuccess.value = result.getOrThrow()

                } else {
                    failureMessage.value = result.exceptionOrNull()?.message
                }
            }
        }
    }
}