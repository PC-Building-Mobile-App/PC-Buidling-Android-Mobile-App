package com.iti.data.auth.datasource

import com.iti.data.BuildConfig
import com.iti.data.auth.model.AuthResponseDto
import com.iti.data.auth.model.ErrorResponseDto
import com.iti.data.auth.model.LoginRequestDto
import com.iti.data.auth.model.RegisterRequestDto
import com.iti.domain.exceptions.AuthException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRemoteDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : AuthRemoteDataSource {

    override suspend fun login(email: String, password: String): Result<AuthResponseDto> =
        runCatching {
            val response = httpClient.post("${BuildConfig.APP_API_BASE_URL}/api/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequestDto(email = email, password = password))
            }
            response.toAuthResult().getOrThrow()
        }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
    ): Result<AuthResponseDto> = runCatching {
        val response = httpClient.post("${BuildConfig.APP_API_BASE_URL}/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequestDto(name = name, email = email, password = password))
        }
        response.toAuthResult().getOrThrow()
    }

    private suspend fun HttpResponse.toAuthResult(): Result<AuthResponseDto> {
        return when (status) {
            HttpStatusCode.OK, HttpStatusCode.Created ->
                Result.success(body<AuthResponseDto>())

            HttpStatusCode.Unauthorized ->
                Result.failure(AuthException.InvalidCredentials)

            HttpStatusCode.Conflict ->
                Result.failure(AuthException.EmailAlreadyExists)

            HttpStatusCode.BadRequest -> {
                val error = runCatching { body<ErrorResponseDto>() }.getOrNull()
                val message = error?.message?.takeIf { it.isNotBlank() }
                Result.failure(
                    if (message != null) Exception(message) else AuthException.InvalidCredentials,
                )
            }

            else ->
                Result.failure(Exception("Request failed with status $status"))
        }
    }
}