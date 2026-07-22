package com.iti.data.components.datasource

import com.iti.data.components.model.ComponentDataModel
import com.iti.data.util.PageResponse

interface ComponentRemoteDataSource {
    suspend fun getRandomComponents(category: String, limit: Int): Result<List<ComponentDataModel>>

    suspend fun getAllProducts(
        page: Int,
        size: Int,
        category: String? = null
    ): Result<PageResponse<ComponentDataModel>>

    suspend fun searchProducts(
        keyword: String?,
        category: String?,
        minPrice: Double?,
        maxPrice: Double?,
        page: Int,
        size: Int
    ): Result<PageResponse<ComponentDataModel>>

}
