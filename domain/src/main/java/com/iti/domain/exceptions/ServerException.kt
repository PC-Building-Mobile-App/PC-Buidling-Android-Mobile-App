package com.iti.domain.exceptions

sealed class ServerException(
    override val message: String,
    val code: Int
) : AppException(message) {
    class Generic(message: String, code: Int) : ServerException(message, code)
    class Unauthorized(message: String = "Unauthorized access") : ServerException(message, 401)
    class Forbidden(message: String = "Access forbidden") : ServerException(message, 403)
    class NotFound(message: String = "Resource not found") : ServerException(message, 404)
    class InternalServerError(message: String = "Internal server error") : ServerException(message, 500)
    class ServiceUnavailable(message: String = "Service temporarily unavailable") : ServerException(message, 503)
}