package com.saim.MedicalStoreModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saim.domain.entities.MyCartData
import com.saim.domain.repositories.MyCartRepositoryContract
import com.saim.domain.repositories.StorageRepositoryContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class MyCartViewModel @javax.inject.Inject constructor(
    val medicinerepository: MyCartRepositoryContract,
    val storageRepository: StorageRepositoryContract
) : ViewModel(){
    val isSuccessfullyDeleted = MutableStateFlow<Boolean?>(null)
    val isSuccessfullySaved = MutableStateFlow<Boolean?>(null)
    val failureMessage = MutableStateFlow<String?>(null)


    fun savemycart(uid: String, mycart: MyCartData) {
        viewModelScope.launch {
            val result = medicinerepository.savemycart(uid, mycart)
            if (result.isSuccess) {
                isSuccessfullySaved.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
    fun deletemycart(uid: String, medid: String) {
        viewModelScope.launch {
            val result = medicinerepository.deletemycart(uid, medid)
            if (result.isSuccess) {
                isSuccessfullyDeleted.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
    fun getTotalPrice(): Double {
        return data.value?.sumOf {
            val unitPrice = it.price.toDoubleOrNull() ?: 0.0
            val qty = it.quantity.toIntOrNull() ?: 0
            unitPrice * qty
        } ?: 0.0
    }

    val mycartrepo = medicinerepository

    val failuremessge = MutableStateFlow<String?>(null)
    val data= MutableStateFlow<List<MyCartData>?>(null)
    // val dataone= MutableStateFlow<Medicine?>(null)


    // Call readmycart(uid) from the UI when user is available



    fun readmycart(uid: String){
        viewModelScope.launch {
            mycartrepo.getmycart(uid).catch {
                failuremessge.value=it.message
            }

                .collect{
                    data.value=it
                }
        }
    }

}