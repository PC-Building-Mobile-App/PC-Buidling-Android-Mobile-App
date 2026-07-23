package com.iti.domain.ai.repository

import kotlinx.coroutines.flow.Flow

interface AiRepository {
    fun getAiOverview(query: String): Flow<Result<String>>
}
