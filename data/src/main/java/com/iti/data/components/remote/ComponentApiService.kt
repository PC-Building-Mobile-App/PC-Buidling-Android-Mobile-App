package com.iti.data.components.remote

import com.iti.data.components.model.ComponentDataModel
import com.iti.data.util.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class ComponentApiService @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = "https://pc-builder-api-production-f3c6.up.railway.app/api"

    suspend fun getRandomDeals(category: String, limit: Int): ApiResponse<List<ComponentDataModel>> {
        return httpClient.get("$baseUrl/products/deals/random") {
            parameter("category", category)
            parameter("limit", limit)
        }.body()
    }
}