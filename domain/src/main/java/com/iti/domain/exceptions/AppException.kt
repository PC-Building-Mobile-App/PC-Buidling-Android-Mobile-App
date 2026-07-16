package com.iti.domain.exceptions

sealed class AppException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause) {
    class Unknown(message: String?, cause: Throwable? = null) : AppException(message, cause)
}
