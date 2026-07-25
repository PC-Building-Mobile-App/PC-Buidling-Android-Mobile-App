package com.iti.data.util
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T? = null
)