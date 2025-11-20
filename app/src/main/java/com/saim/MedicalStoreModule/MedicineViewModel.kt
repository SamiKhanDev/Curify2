package com.saim.MedicalStoreModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saim.domain.entities.Drugs
import com.saim.domain.repositories.MedicineRepositoryContract
import com.saim.domain.repositories.StorageRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel

class MedicineViewModel @javax.inject.Inject constructor(
    val medicinerepository: MedicineRepositoryContract,
    val storageRepository: StorageRepositoryContract
) : ViewModel(){
    val isSuccessfullyDeleted = MutableStateFlow<Boolean?>(null)
    val isSuccessfullySaved = MutableStateFlow<Boolean?>(null)
    val failureMessage = MutableStateFlow<String?>(null)

    fun uploadImageAndmedicine(imagePath: String, medicine: Drugs) {
        storageRepository.uploadFile(imagePath, onComplete ={ success, result ->
            if (success) {
                medicine.image=result!!
                savemedicine(medicine)
            }
            else failureMessage.value=result
        })
    }
    fun savemedicine(medicine: Drugs) {
        viewModelScope.launch {
            val result = medicinerepository.savemedicine(medicine)
            if (result.isSuccess) {
                isSuccessfullySaved.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
    fun deletePetShop(medid: String) {
        viewModelScope.launch {
            val result = medicinerepository.deletemedicine(medid)
            if (result.isSuccess) {
                isSuccessfullyDeleted.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }


    fun updatemed(med: Drugs) {
        viewModelScope.launch {
            val result = medicinerepository.updatemedicine(med)
            if (result.isSuccess) {
                isSuccessfullySaved.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}