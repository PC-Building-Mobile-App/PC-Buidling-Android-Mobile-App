package com.iti.data.aichat.repository

import com.iti.data.aichat.datasource.AiChatDataSource
import com.iti.data.aichat.mapper.toDomain
import com.iti.domain.aichat.model.AiChatResponse
import com.iti.domain.aichat.repository.AiChatRepository
import javax.inject.Inject

class AiChatRepositoryImpl @Inject constructor(
    private val dataSource: AiChatDataSource,
) : AiChatRepository {

    override suspend fun sendMessage(sessionId: Long?, message: String): Result<AiChatResponse> {
        return dataSource.sendMessage(sessionId, message).map { it.toDomain() }
    }
}
