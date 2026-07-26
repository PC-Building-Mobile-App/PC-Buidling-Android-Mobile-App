package com.iti.data.aichat.mapper

import com.iti.data.aichat.model.AiChatResponseDto
import com.iti.data.components.mapper.toDomain
import com.iti.domain.aichat.model.AiChatResponse

fun AiChatResponseDto.toDomain(): AiChatResponse = AiChatResponse(
    sessionId = sessionId,
    reply = reply,
    mentionedProducts = mentionedProducts.mapNotNull { it.toDomain() },
    timestamp = timestamp ?: "",
)
