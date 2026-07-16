package com.iti.domain.exceptions

sealed class AuthException(message: String?) : AppException(message) {
    data object Unauthorized : AuthException("Session expired. Please login again.")
    data object InvalidCredentials : AuthException("Invalid email or password.")
}
