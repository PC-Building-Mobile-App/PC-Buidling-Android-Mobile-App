package com.iti.presentation.aichat.mapper

import com.iti.domain.aichat.model.AiChatResponse
import com.iti.presentation.aichat.model.AiChatMessageUiModel
import com.iti.presentation.core.pccomponents.mapper.toUiModel
import com.iti.presentation.core.pccomponents.mapper.toUiModels
import java.util.UUID

fun AiChatResponse.toUiMessage(): AiChatMessageUiModel = AiChatMessageUiModel(
    id = UUID.randomUUID().toString(),
    content = reply,
    isFromUser = false,
    mentionedProducts = mentionedProducts.toUiModels(),
    timestamp = System.currentTimeMillis(),
)

fun createUserMessage(content: String): AiChatMessageUiModel = AiChatMessageUiModel(
    id = UUID.randomUUID().toString(),
    content = content,
    isFromUser = true,
)
