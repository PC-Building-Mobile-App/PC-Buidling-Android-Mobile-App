package com.iti.presentation

import androidx.lifecycle.viewModelScope
import com.iti.domain.auth.usecase.ObserveAuthStateUseCase
import com.iti.domain.onboarding.usecase.GetOnboardingStatusUseCase
import com.iti.domain.settings.usecase.ObserveAppThemeUseCase
import com.iti.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val observeAppThemeUseCase: ObserveAppThemeUseCase,
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    init {
        onEvent(MainContract.Event.CheckAppState)
    }

    override fun createInitialState(): MainContract.State = MainContract.State()

    override fun onEvent(event: MainContract.Event) {
        when (event) {
            MainContract.Event.CheckAppState -> loadAppState()
        }
    }

    private fun loadAppState() {
        viewModelScope.launch {
            combine(
                getOnboardingStatusUseCase(),
                observeAuthStateUseCase(),
                observeAppThemeUseCase(),
            ) { hasSeenOnboarding, isAuthenticated, theme ->
                MainContract.State(
                    isLoading = false,
                    hasSeenOnboarding = hasSeenOnboarding,
                    isAuthenticated = isAuthenticated,
                    theme = theme,
                )
            }.collect { newState ->
                updateState { newState }
            }
        }
    }
}