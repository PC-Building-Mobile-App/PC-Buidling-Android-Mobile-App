package com.iti.data.components.remote

import com.iti.data.components.model.ComponentDataModel
import com.iti.data.util.ApiResponse
import com.iti.data.util.PageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class ComponentApiService @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = "https://pc-builder-api-production-f3c6.up.railway.app"+"/api"

    suspend fun getRandomDeals(category: String, limit: Int): ApiResponse<List<ComponentDataModel>> {
        return httpClient.get("$baseUrl/products/deals/random") {
            parameter("category", category)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getAllProducts(
        page: Int,
        size: Int,
        category: String? = null
    ): ApiResponse<PageResponse<ComponentDataModel>> {
        return httpClient.get("$baseUrl/products") {
            parameter("page", page)
            parameter("size", size)
            category?.let { parameter("category", it) }
        }.body()
    }

    suspend fun searchProducts(
        keyword: String?,
        category: String?,
        minPrice: Double?,
        maxPrice: Double?,
        page: Int,
        size: Int
    ): ApiResponse<PageResponse<ComponentDataModel>> {
        return httpClient.get("$baseUrl/products/search") {
            keyword?.let { parameter("keyword", it) }
            category?.let { parameter("category", it) }
            minPrice?.let { parameter("minPrice", it) }
            maxPrice?.let { parameter("maxPrice", it) }
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

}
