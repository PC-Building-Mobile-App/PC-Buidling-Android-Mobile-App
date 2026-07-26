package com.iti.data.aichat.model

import com.iti.data.components.model.ComponentDataModel
import kotlinx.serialization.Serializable

@Serializable
data class AiChatResponseDto(
    val sessionId: Long,
    val reply: String,
    val mentionedProducts: List<ComponentDataModel> = emptyList(),
    val timestamp: String? = null,
)
