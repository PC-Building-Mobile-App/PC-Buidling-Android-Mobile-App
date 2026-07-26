package com.iti.data.aichat.datasource

import com.iti.data.aichat.model.AiChatResponseDto

interface AiChatDataSource {
    suspend fun sendMessage(sessionId: Long?, message: String): Result<AiChatResponseDto>
}
