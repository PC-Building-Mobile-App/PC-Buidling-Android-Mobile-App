package com.iti.presentation.partdetails

import com.iti.presentation.core.pccomponents.model.ComponentUiModel

object PartDetailsContract {
    data class State(
        val component: ComponentUiModel? = null,
        val specs: Map<String, String> = emptyMap(),
        val isAiLoading: Boolean = false,
        val aiExplanation: String? = null,
    )

    sealed interface Event {
        data object BackClicked : Event
        data object AddToBuildClicked : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class NavigateToBuildGeneration(val component: ComponentUiModel) : Effect
    }
}