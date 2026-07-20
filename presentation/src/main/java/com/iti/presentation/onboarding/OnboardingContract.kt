package com.iti.presentation.onboarding

import com.iti.presentation.core.UiText

object OnboardingContract {

    data class State(
        val isLoading: Boolean = true,
        val hasCompletedOnboarding: Boolean = false,
        val errorMessage: UiText? = null
    )

    sealed interface Event {
        data object CompleteOnboarding : Event
    }

    sealed interface Effect {
        data object NavigateToAuth : Effect
    }
}