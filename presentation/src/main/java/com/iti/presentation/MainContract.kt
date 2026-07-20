package com.iti.presentation

object MainContract {
    data class State(
        val isLoading: Boolean = true,
        val hasSeenOnboarding: Boolean = false,
        val isAuthenticated: Boolean = false
    )
    sealed interface Event {
        data object CheckAppState : Event
        data object AuthSuccess : Event
    }
    sealed interface Effect
}