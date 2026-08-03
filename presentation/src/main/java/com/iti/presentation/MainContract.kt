package com.iti.presentation

import com.iti.domain.settings.model.AppThemePreference

object MainContract {
    data class State(
        val isLoading: Boolean = true,
        val hasSeenOnboarding: Boolean = false,
        val isAuthenticated: Boolean = false,
        val theme: AppThemePreference = AppThemePreference.DARK,
    )

    sealed interface Event {
        data object CheckAppState : Event
    }

    sealed interface Effect
}