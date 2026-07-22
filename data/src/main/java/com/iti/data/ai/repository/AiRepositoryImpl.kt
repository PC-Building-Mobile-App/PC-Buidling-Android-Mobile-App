package com.iti.data.ai.repository

import com.iti.data.ai.datasource.AiDataSource
import com.iti.domain.ai.repository.AiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val aiDataSource: AiDataSource
) : AiRepository {

    override fun getAiOverview(query: String): Flow<Result<String>> = flow {
        emit(aiDataSource.generateOverview(query))
    }
}
