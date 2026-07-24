package com.iti.data.builds.remote

import com.iti.data.BuildConfig
import com.iti.data.auth.local.AuthTokenStorage
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.CompatibilityCheckRequestDto
import com.iti.data.builds.model.CompatibilityReportDto
import com.iti.data.builds.model.GenerateBuildRequestDto
import com.iti.data.builds.model.GeneratedBuildDto
import com.iti.data.builds.model.SaveBuildRequestDto
import com.iti.data.util.ApiResponse
import com.iti.data.util.PageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildsApiService @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenStorage: AuthTokenStorage,
) {
    private val baseUrl = "${BuildConfig.APP_API_BASE_URL}/api"

    private suspend fun getAuthHeader(): String? {
        val token = tokenStorage.authToken.firstOrNull()
        return if (!token.isNullOrBlank()) "Bearer $token" else null
    }

    suspend fun generateBuild(request: GenerateBuildRequestDto): ApiResponse<GeneratedBuildDto> {
        val authHeader = getAuthHeader()
        return httpClient.post("$baseUrl/ai/build-generator") {
            contentType(ContentType.Application.Json)
            authHeader?.let { header(HttpHeaders.Authorization, it) }
            setBody(request)
        }.body()
    }

    suspend fun checkCompatibility(request: CompatibilityCheckRequestDto): ApiResponse<CompatibilityReportDto> {
        val authHeader = getAuthHeader()
        return httpClient.post("$baseUrl/ai/compatibility-check") {
            contentType(ContentType.Application.Json)
            authHeader?.let { header(HttpHeaders.Authorization, it) }
            setBody(request)
        }.body()
    }

    suspend fun saveBuild(request: SaveBuildRequestDto): ApiResponse<BuildDto> {
        val authHeader = getAuthHeader()
        return httpClient.post("$baseUrl/bundles") {
            contentType(ContentType.Application.Json)
            authHeader?.let { header(HttpHeaders.Authorization, it) }
            setBody(request)
        }.body()
    }

    suspend fun getBundles(
        type: String?,
        page: Int,
        size: Int
    ): ApiResponse<PageResponse<BuildDto>> {
        val authHeader = getAuthHeader()
        return httpClient.get("$baseUrl/bundles") {
            authHeader?.let { header(HttpHeaders.Authorization, it) }
            type?.let { parameter("type", it) }
            parameter("page", page)
            parameter("size", size)
        }.body()
    }
}
