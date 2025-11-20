package com.saim.domain.entities

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String? = null,
    val isAdmin: Boolean = false
)

