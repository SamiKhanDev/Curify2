package com.saim.domain.entities

data class PaymentMethod(
    val id: String = "",
    val method: String = "", // e.g., Jazzcash, EasyPaisa
    val name: String = "",
    val phoneNumber: String = "",
    val timestampMs: Long = 0L
)

