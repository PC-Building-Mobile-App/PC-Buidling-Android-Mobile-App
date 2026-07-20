package com.iti.presentation.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.auth.usecase.LoginUseCase
import com.iti.domain.auth.usecase.RegisterUseCase
import com.iti.domain.exceptions.AuthException
import com.iti.presentation.auth.AuthContract
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
) : BaseViewModel<AuthContract.Event, AuthContract.State, AuthContract.Effect>() {

    override fun createInitialState(): AuthContract.State = AuthContract.State()

    override fun onEvent(event: AuthContract.Event) {
        when (event) {
            is AuthContract.Event.NameChanged -> updateState { it.copy(name = event.name, errorMessage = null) }
            is AuthContract.Event.EmailChanged -> updateState { it.copy(email = event.email, errorMessage = null) }
            is AuthContract.Event.PasswordChanged -> updateState { it.copy(password = event.password, errorMessage = null) }
            AuthContract.Event.ToggleMode -> updateState {
                it.copy(
                    isLoginMode = !it.isLoginMode,
                    errorMessage = null,
                )
            }
            AuthContract.Event.DismissError -> updateState { it.copy(errorMessage = null) }
            AuthContract.Event.Submit -> submit()
        }
    }

    private fun submit() {
        val current = state.value
        if (!current.isSubmitEnabled) return

        updateState { it.copy(isLoading = true, errorMessage = null) }

        val flow = if (current.isLoginMode) {
            loginUseCase(current.email, current.password)
        } else {
            registerUseCase(current.name, current.email, current.password)
        }

        flow
            .onEach { user ->
                updateState { it.copy(isLoading = false) }
                sendEffect(AuthContract.Effect.NavigateToHome(userName = user.name))
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
        else -> "Something went wrong. Please try again."
    }
}