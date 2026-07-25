package com.iti.data.util

import android.util.Log
import com.iti.data.BuildConfig
import com.iti.domain.exceptions.AppException
import com.iti.domain.exceptions.NetworkException
import com.iti.domain.exceptions.ServerException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toAppException(): AppException {
    if (BuildConfig.DEBUG) {
        Log.e("APP_EXCEPTION", "An unexpected error occurred", this)
    }
    return when (this) {
        is SerializationException -> ServerException.Generic("Invalid server response format", 0)
        is ResponseException -> ServerException.Generic(
            "Server error occurred",
            this.response.status.value
        )

        is ServerException -> ServerException.Generic(
            "Server error occurred",
            this.code
        )

        is HttpRequestTimeoutException,
        is SocketTimeoutException -> NetworkException.Timeout

        is IOException -> NetworkException.NoInternet
        is AppException -> this
        else -> AppException.Unknown(this.message ?: "An unexpected error occurred", this)
    }
}