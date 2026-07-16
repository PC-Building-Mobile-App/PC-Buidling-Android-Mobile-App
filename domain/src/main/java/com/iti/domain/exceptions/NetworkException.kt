package com.iti.domain.exceptions

sealed class NetworkException(message: String?) : AppException(message) {
    data object NoInternet : NetworkException("No internet connection. Please check your network.")
    data object Timeout : NetworkException("The server is taking too long to respond.")
}
