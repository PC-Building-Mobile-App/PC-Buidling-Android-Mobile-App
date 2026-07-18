package com.iti.data.hardwarenews.remote

import com.iti.data.BuildConfig
import com.iti.data.hardwarenews.model.ArticleDataModel
import com.iti.data.hardwarenews.model.CurrentsApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import javax.inject.Inject

class HardwareNewsApiService @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = BuildConfig.CURRENTS_API_BASE_URL
    private val apiKey = BuildConfig.CURRENTS_API_KEY

    suspend fun searchHardwareNews(): CurrentsApiResponse {
        return httpClient.get("$baseUrl/search") {
            header("Authorization", apiKey)
            parameter("keywords", "semiconductor OR GPU OR CPU OR hardware")
            parameter("language", "en")
            parameter("sortBy", "publishedAt")
        }.body()
    }

    suspend fun getArticleById(id: String): ArticleDataModel? {
        val response = searchHardwareNews()
        return response.news.find { it.id == id }
    }
}