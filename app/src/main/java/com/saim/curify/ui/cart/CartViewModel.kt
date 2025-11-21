package com.saim.curify.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Cart ViewModel
 * TODO: Integrate with cart repository
 */
class CartViewModel : ViewModel() {
    
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems
    
    private val _subtotal = MutableLiveData<Double>()
    val subtotal: LiveData<Double> = _subtotal
    
    private val _taxes = MutableLiveData<Double>()
    val taxes: LiveData<Double> = _taxes
    
    private val _total = MutableLiveData<Double>()
    val total: LiveData<Double> = _total
    
    fun loadCart() {
        viewModelScope.launch {
            // TODO: Load from repository
            _cartItems.value = getMockCartItems()
            calculateTotals()
        }
    }
    
    fun updateQuantity(itemId: String, quantity: Int) {
        // TODO: Update quantity in repository
        calculateTotals()
    }
    
    fun removeItem(itemId: String) {
        // TODO: Remove from repository
        calculateTotals()
    }
    
    private fun calculateTotals() {
        val items = _cartItems.value ?: emptyList()
        val subtotalValue = items.sumOf { it.price * it.quantity }
        val taxesValue = subtotalValue * 0.05 // 5% tax
        val totalValue = subtotalValue + taxesValue
        
        _subtotal.value = subtotalValue
        _taxes.value = taxesValue
        _total.value = totalValue
    }
    
    private fun getMockCartItems(): List<CartItem> {
        return listOf(
            CartItem("1", "Medicine 1", "500mg", 999.0, "", 2),
            CartItem("2", "Medicine 2", "250mg", 599.0, "", 1)
        )
    }
}

data class CartItem(
    val id: String,
    val name: String,
    val weight: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int
)

