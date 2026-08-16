package com.iti.domain.auth.usecase

object UsernameValidator {

    private const val MIN_LENGTH = 3
    private const val MAX_LENGTH = 25

    fun validate(name: String): String? {
        if (name.isEmpty()) return null
        if (name.length < MIN_LENGTH) {
            return "Username must be at least $MIN_LENGTH characters"
        }
        if (name.length > MAX_LENGTH) {
            return "Username must be at most $MAX_LENGTH characters"
        }
        return null
    }
}
