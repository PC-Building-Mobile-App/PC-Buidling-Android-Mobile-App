package com.iti.domain.auth.model

data class AuthUser(
    val id: Int,
    val name: String,
    val email: String,
    val token: String,
    val tokenType: String,
)