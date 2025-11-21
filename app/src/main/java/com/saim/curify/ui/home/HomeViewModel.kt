package com.saim.curify.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Home ViewModel
 * Manages home screen data and business logic
 * TODO: Integrate with repository/data source
 */
class HomeViewModel : ViewModel() {
    
    private val _greeting = MutableLiveData<String>()
    val greeting: LiveData<String> = _greeting
    
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories
    
    private val _popularMedicines = MutableLiveData<List<Medicine>>()
    val popularMedicines: LiveData<List<Medicine>> = _popularMedicines
    
    private val _recommendedMedicines = MutableLiveData<List<Medicine>>()
    val recommendedMedicines: LiveData<List<Medicine>> = _recommendedMedicines
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: Fetch data from repository/API
                // Mock data for now
                _greeting.value = "Hi, User"
                _categories.value = getMockCategories()
                _popularMedicines.value = getMockMedicines()
                _recommendedMedicines.value = getMockMedicines()
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun getMockCategories(): List<Category> {
        return listOf(
            Category("1", "Tablets", ""),
            Category("2", "Syrups", ""),
            Category("3", "Capsules", ""),
            Category("4", "Injections", "")
        )
    }
    
    private fun getMockMedicines(): List<Medicine> {
        return listOf(
            Medicine("1", "Medicine 1", "500mg", 999.0, "", true),
            Medicine("2", "Medicine 2", "250mg", 599.0, "", true),
            Medicine("3", "Medicine 3", "1000mg", 1299.0, "", true)
        )
    }
}

// Data classes
data class Category(
    val id: String,
    val name: String,
    val iconUrl: String
)

data class Medicine(
    val id: String,
    val name: String,
    val weight: String,
    val price: Double,
    val imageUrl: String,
    val inStock: Boolean
)

