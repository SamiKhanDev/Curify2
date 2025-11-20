package com.saim.domain.entities

data class Medicine(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val unitPrice: Double = 0.0,
    val stockQuantity: Int = 0,
    val weight: String = "",
    val type: String = "",
    val imageUrl: String = "",
    val status: String = ""
)

