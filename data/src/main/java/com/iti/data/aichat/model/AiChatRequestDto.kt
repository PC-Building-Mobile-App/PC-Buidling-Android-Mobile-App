package com.iti.data.aichat.model

import kotlinx.serialization.Serializable

@Serializable
data class AiChatRequestDto(
    val sessionId: Long? = null,
    val message: String,
)
