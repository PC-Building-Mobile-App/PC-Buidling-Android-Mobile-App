package com.iti.domain.auth.usecase

object PasswordValidator {

    private const val MIN_LENGTH = 8

    fun validateStrength(password: String): String? {
        if (password.length < MIN_LENGTH) {
            return "Password must be at least $MIN_LENGTH characters"
        }
        if (password.none { it.isDigit() }) {
            return "Password must contain at least one number"
        }
        if (password.none { it.isLetter() }) {
            return "Password must contain at least one letter"
        }
        if (password.none { it.isUpperCase() }) {
            return "Password must contain at least one uppercase letter"
        }
        return null
    }

    fun validateMatch(password: String, confirmPassword: String): String? {
        if (confirmPassword.isEmpty()) return null
        return if (password != confirmPassword) "Passwords do not match" else null
    }
}