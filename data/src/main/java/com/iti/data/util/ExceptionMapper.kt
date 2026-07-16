package com.iti.data.util

import com.iti.domain.exceptions.AppException
import com.iti.domain.exceptions.NetworkException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toAppException(): AppException {
    return when (this) {
        is SocketTimeoutException -> NetworkException.Timeout
        is IOException -> NetworkException.NoInternet
        is AppException -> this
        else -> AppException.Unknown(this.message ?: "An unexpected error occurred", this)
    }
}
