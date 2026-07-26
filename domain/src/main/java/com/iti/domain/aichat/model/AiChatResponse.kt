package com.iti.domain.aichat.model

import com.iti.domain.components.model.Component

data class AiChatResponse(
    val sessionId: Long,
    val reply: String,
    val mentionedProducts: List<Component>,
    val timestamp: String,
)
