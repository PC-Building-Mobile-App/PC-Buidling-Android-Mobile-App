package com.iti.domain.auth.usecase

object EmailValidator {

    private val EMAIL_REGEX = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
    )

    fun validate(email: String): String? {
        if (email.isEmpty()) return null
        return if (!EMAIL_REGEX.matches(email)) "Enter a valid email address" else null
    }
}