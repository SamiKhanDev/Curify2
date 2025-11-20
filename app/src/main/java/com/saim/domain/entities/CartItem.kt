package com.saim.domain.entities

data class CartItem(
    val id: String = "",
    val medicineId: String = "",
    val title: String = "",
    val unitPrice: Double = 0.0,
    val quantity: Int = 0,
    val weight: String = "",
    val imageUrl: String = ""
)

