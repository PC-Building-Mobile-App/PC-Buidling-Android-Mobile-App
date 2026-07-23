package com.iti.data.ai.datasource

interface AiDataSource {
    suspend fun generateOverview(query: String): Result<String>
}
