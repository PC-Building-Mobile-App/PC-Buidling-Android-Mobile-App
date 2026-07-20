package com.iti.presentation.auth

object AuthContract {

    data class State(
        val isLoading: Boolean = false,
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val isLoginMode: Boolean = true,
        val errorMessage: String? = null,
    ) {
        val isSubmitEnabled: Boolean
            get() = !isLoading &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    (isLoginMode || name.isNotBlank())
    }

    sealed interface Event {
        data class NameChanged(val name: String) : Event
        data class EmailChanged(val email: String) : Event
        data class PasswordChanged(val password: String) : Event
        data object ToggleMode : Event
        data object Submit : Event
        data object DismissError : Event
    }

    sealed interface Effect {
        data class NavigateToHome(val userName: String) : Effect
    }
}