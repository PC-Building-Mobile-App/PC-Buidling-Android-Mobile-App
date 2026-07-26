package com.iti.domain.aichat.repository

import com.iti.domain.aichat.model.AiChatResponse

interface AiChatRepository {
    suspend fun sendMessage(sessionId: Long?, message: String): Result<AiChatResponse>
}
