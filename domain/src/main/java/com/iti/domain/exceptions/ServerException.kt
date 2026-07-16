package com.iti.domain.exceptions

sealed class ServerException(
    override val message: String,
    val code: Int
) : AppException(message)
