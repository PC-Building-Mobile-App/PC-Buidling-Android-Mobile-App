package com.iti.presentation.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.auth.usecase.LoginUseCase
import com.iti.domain.auth.usecase.RegisterUseCase
import com.iti.domain.exceptions.AuthException
import com.iti.presentation.auth.AuthContract.Effect
import com.iti.presentation.auth.AuthContract.Event
import com.iti.presentation.auth.AuthContract.State
import com.iti.domain.auth.usecase.EmailValidator
import com.iti.domain.auth.usecase.PasswordValidator
import com.iti.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State()

    override fun onEvent(event: Event) {
        when (event) {
            is Event.NameChanged -> updateState { it.copy(name = event.name, errorMessage = null) }
            is Event.EmailChanged -> onEmailChanged(event.email)
            is Event.PasswordChanged -> onPasswordChanged(event.password)
            is Event.ConfirmPasswordChanged -> onConfirmPasswordChanged(event.confirmPassword)
            Event.TogglePasswordVisibility -> updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            Event.ToggleConfirmPasswordVisibility -> updateState {
                it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
            }
            Event.ToggleMode -> updateState {
                it.copy(
                    isLoginMode = !it.isLoginMode,
                    errorMessage = null,
                    passwordError = null,
                    confirmPasswordError = null,
                    confirmPassword = "",
                )
            }
            Event.DismissError -> updateState { it.copy(errorMessage = null) }
            Event.Submit -> submit()
        }
    }

    private fun onEmailChanged(email: String) {
        updateState {
            it.copy(
                email = email,
                errorMessage = null,
                emailError = EmailValidator.validate(email),
            )
        }
    }

    private fun onPasswordChanged(password: String) {
        updateState { current ->
            val passwordError = if (current.isLoginMode) null else PasswordValidator.validateStrength(password)
            val confirmError = if (current.isLoginMode) {
                null
            } else {
                PasswordValidator.validateMatch(password, current.confirmPassword)
            }
            current.copy(
                password = password,
                errorMessage = null,
                passwordError = passwordError,
                confirmPasswordError = confirmError,
            )
        }
    }

    private fun onConfirmPasswordChanged(confirmPassword: String) {
        updateState { current ->
            current.copy(
                confirmPassword = confirmPassword,
                errorMessage = null,
                confirmPasswordError = PasswordValidator.validateMatch(current.password, confirmPassword),
            )
        }
    }

    private fun submit() {
        val current = state.value
        if (!current.isSubmitEnabled) return
        val emailError = EmailValidator.validate(current.email)
        if (emailError != null) {
            updateState { it.copy(emailError = emailError) }
            return
        }

        if (!current.isLoginMode) {
            val passwordError = PasswordValidator.validateStrength(current.password)
            val confirmError = PasswordValidator.validateMatch(current.password, current.confirmPassword)
            if (passwordError != null || confirmError != null) {
                updateState { it.copy(passwordError = passwordError, confirmPasswordError = confirmError) }
                return
            }
        }

        updateState { it.copy(isLoading = true, errorMessage = null) }

        val flow = if (current.isLoginMode) {
            loginUseCase(current.email, current.password)
        } else {
            registerUseCase(current.name, current.email, current.password)
        }

        flow
            .onEach { user ->
                updateState { it.copy(isLoading = false) }
                sendEffect(Effect.NavigateToHome(userName = user.name))
            }
            .catch { throwable ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.toErrorMessage(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun Throwable.toErrorMessage(): String = when (this) {
        is AuthException.InvalidCredentials -> message.orEmpty()
        is AuthException.EmailAlreadyExists -> message.orEmpty()
        is AuthException.Unauthorized -> message.orEmpty()
        else -> message?.takeIf { it.isNotBlank() } ?: "Something went wrong. Please try again."
    }
}