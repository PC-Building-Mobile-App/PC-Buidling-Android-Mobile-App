package com.iti.data.aichat.remote

import com.iti.data.BuildConfig
import com.iti.data.aichat.model.AiChatRequestDto
import com.iti.data.aichat.model.AiChatResponseDto
import com.iti.data.auth.local.AuthTokenStorage
import com.iti.data.util.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiChatApiService @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenStorage: AuthTokenStorage,
) {
    private val baseUrl = "${BuildConfig.APP_API_BASE_URL}/api"

    private suspend fun getAuthHeader(): String? {
        val token = tokenStorage.authToken.firstOrNull()
        return if (!token.isNullOrBlank()) "Bearer $token" else null
    }

    suspend fun sendMessage(request: AiChatRequestDto): ApiResponse<AiChatResponseDto> {
        val authHeader = getAuthHeader()
        return httpClient.post("$baseUrl/ai/chat") {
            contentType(ContentType.Application.Json)
            authHeader?.let { header(HttpHeaders.Authorization, it) }
            setBody(request)
        }.body()
    }
}
