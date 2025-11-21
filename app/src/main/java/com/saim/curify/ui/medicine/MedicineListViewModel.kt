package com.saim.curify.ui.medicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Medicine List ViewModel
 * TODO: Integrate with repository
 */
class MedicineListViewModel : ViewModel() {
    
    private val _medicines = MutableLiveData<List<Medicine>>()
    val medicines: LiveData<List<Medicine>> = _medicines
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun loadMedicines() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: Fetch from repository
                _medicines.value = getMockMedicines()
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun searchMedicines(query: String) {
        viewModelScope.launch {
            // TODO: Search medicines
            _medicines.value = getMockMedicines().filter { 
                it.name.contains(query, ignoreCase = true) 
            }
        }
    }
    
    private fun getMockMedicines(): List<Medicine> {
        return listOf(
            Medicine("1", "Medicine 1", "500mg", 999.0, "", true),
            Medicine("2", "Medicine 2", "250mg", 599.0, "", true)
        )
    }
}

data class Medicine(
    val id: String,
    val name: String,
    val weight: String,
    val price: Double,
    val imageUrl: String,
    val inStock: Boolean
)

