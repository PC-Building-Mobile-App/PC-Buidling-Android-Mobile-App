package com.iti.domain.aichat.usecase

import com.iti.domain.aichat.model.AiChatResponse
import com.iti.domain.aichat.repository.AiChatRepository
import javax.inject.Inject

class SendAiChatMessageUseCase @Inject constructor(
    private val repository: AiChatRepository,
) {
    suspend operator fun invoke(sessionId: Long?, message: String): Result<AiChatResponse> {
        return repository.sendMessage(sessionId, message)
    }
}
