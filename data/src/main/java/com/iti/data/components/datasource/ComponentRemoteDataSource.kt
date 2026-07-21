package com.iti.data.components.datasource

import com.iti.data.components.model.ComponentDataModel

interface ComponentRemoteDataSource {
    suspend fun getRandomComponents(category: String, limit: Int): Result<List<ComponentDataModel>>
}