package com.iti.data.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    @SerialName("status") val status: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: AuthDataDto,
)

@Serializable
data class AuthDataDto(
    @SerialName("token") val token: String,
    @SerialName("tokenType") val tokenType: String,
    @SerialName("user") val user: UserDto,
)

@Serializable
data class UserDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("role") val role: String = "USER",
)

@Serializable
data class LoginRequestDto(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)

@Serializable
data class RegisterRequestDto(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)

@Serializable
data class ErrorResponseDto(
    @SerialName("status") val status: Boolean = false,
    @SerialName("message") val message: String = "",
)