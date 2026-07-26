package com.iti.presentation.aichat.model

import com.iti.presentation.core.pccomponents.model.ComponentUiModel

data class AiChatMessageUiModel(
    val id: String,
    val content: String,
    val isFromUser: Boolean,
    val mentionedProducts: List<ComponentUiModel> = emptyList(),
    val timestamp: Long = System.currentTimeMillis(),
)
