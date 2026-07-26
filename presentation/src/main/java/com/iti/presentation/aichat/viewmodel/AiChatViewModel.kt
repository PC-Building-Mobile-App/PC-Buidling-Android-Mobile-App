package com.iti.presentation.aichat.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.aichat.usecase.SendAiChatMessageUseCase
import com.iti.presentation.aichat.AiChatContract.Effect
import com.iti.presentation.aichat.AiChatContract.Event
import com.iti.presentation.aichat.AiChatContract.State
import com.iti.presentation.aichat.mapper.createUserMessage
import com.iti.presentation.aichat.mapper.toUiMessage
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val sendAiChatMessageUseCase: SendAiChatMessageUseCase,
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State()

    override fun onEvent(event: Event) {
        when (event) {
            is Event.UpdateInput -> updateState { it.copy(inputText = event.text) }
            is Event.SendMessage -> sendMessage()
            is Event.NavigateToBuild -> sendEffect(Effect.NavigateToBuild)
            is Event.NavigateToCompare -> sendEffect(Effect.NavigateToCompare)
            is Event.ExportMessage -> exportMessage(event.message)
            is Event.DismissError -> updateState { it.copy(errorMessage = null) }
        }
    }

    private fun sendMessage() {
        val text = state.value.inputText.trim()
        if (text.isBlank()) return

        val userMessage = createUserMessage(text)

        updateState {
            it.copy(
                messages = it.messages + userMessage,
                inputText = "",
                isLoading = true,
                hasStartedChat = true,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            val result = sendAiChatMessageUseCase(
                sessionId = state.value.sessionId,
                message = text,
            )

            result.fold(
                onSuccess = { response ->
                    val aiMessage = response.toUiMessage()
                    updateState {
                        it.copy(
                            messages = it.messages + aiMessage,
                            isLoading = false,
                            sessionId = response.sessionId,
                        )
                    }
                },
                onFailure = { throwable ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.toUiText(),
                        )
                    }
                },
            )
        }
    }

    private fun exportMessage(message: com.iti.presentation.aichat.model.AiChatMessageUiModel) {
        sendEffect(Effect.ShareMessage(message))
    }
}
