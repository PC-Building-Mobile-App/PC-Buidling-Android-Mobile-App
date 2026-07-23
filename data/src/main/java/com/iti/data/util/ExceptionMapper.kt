package com.iti.data.util

import com.iti.domain.exceptions.AppException
import com.iti.domain.exceptions.NetworkException
import com.iti.domain.exceptions.ServerException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toAppException(): AppException {
    return when (this) {
        is SerializationException -> ServerException.Generic("Invalid server response format", 0)
        is ResponseException -> {
            val code = this.response.status.value
            val msg = this.message ?: "Server error occurred"

            when (code) {
                401 -> ServerException.Unauthorized(msg)
                403 -> ServerException.Forbidden(msg)
                404 -> ServerException.NotFound(msg)
                500 -> ServerException.InternalServerError(msg)
                503 -> ServerException.ServiceUnavailable(msg)
                else -> ServerException.Generic(msg, code)
            }
        }
        is HttpRequestTimeoutException,
        is SocketTimeoutException -> NetworkException.Timeout
        is IOException -> NetworkException.NoInternet
        is AppException -> this
        else -> AppException.Unknown(this.message ?: "An unexpected error occurred", this)
    }
}