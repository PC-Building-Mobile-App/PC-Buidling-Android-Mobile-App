package com.iti.presentation.auth

object AuthContract {

    data class State(
        val isLoading: Boolean = false,
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isLoginMode: Boolean = true,
        val isPasswordVisible: Boolean = false,
        val isConfirmPasswordVisible: Boolean = false,
        val emailError: String? = null,
        val passwordError: String? = null,
        val nameError: String? = null,
        val confirmPasswordError: String? = null,
        val errorMessage: String? = null,
    ) {
        val isSubmitEnabled: Boolean
            get() = !isLoading &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    emailError == null &&
                    (
                            isLoginMode ||
                                    (
                                            name.isNotBlank() &&
                                                    confirmPassword.isNotBlank() &&
                                                    nameError == null &&
                                                    passwordError == null &&
                                                    confirmPasswordError == null
                                            )
                            )
    }

    sealed interface Event {
        data class NameChanged(val name: String) : Event
        data class EmailChanged(val email: String) : Event
        data class PasswordChanged(val password: String) : Event
        data class ConfirmPasswordChanged(val confirmPassword: String) : Event
        data object TogglePasswordVisibility : Event
        data object ToggleConfirmPasswordVisibility : Event
        data object ToggleMode : Event
        data object Submit : Event
        data object DismissError : Event
    }

    sealed interface Effect {
        data class NavigateToHome(val userName: String) : Effect
    }
}