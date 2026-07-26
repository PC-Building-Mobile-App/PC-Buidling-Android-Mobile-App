package com.iti.presentation.aichat

import com.iti.presentation.aichat.model.AiChatMessageUiModel
import com.iti.presentation.core.UiText

object AiChatContract {

    data class State(
        val messages: List<AiChatMessageUiModel> = emptyList(),
        val inputText: String = "",
        val isLoading: Boolean = false,
        val sessionId: Long? = null,
        val hasStartedChat: Boolean = false,
        val errorMessage: UiText? = null,
    )

    sealed interface Event {
        data class UpdateInput(val text: String) : Event
        data object SendMessage : Event
        data object NavigateToBuild : Event
        data object NavigateToCompare : Event
        data class ExportMessage(val message: AiChatMessageUiModel) : Event
        data object DismissError : Event
    }

    sealed interface Effect {
        data object NavigateToBuild : Effect
        data object NavigateToCompare : Effect
        data class ShareMessage(val message: AiChatMessageUiModel) : Effect
    }
}
