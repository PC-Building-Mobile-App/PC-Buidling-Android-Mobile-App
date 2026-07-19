package com.iti.presentation

import androidx.lifecycle.viewModelScope
import com.iti.domain.onboarding.usecase.GetOnboardingStatusUseCase
import com.iti.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    init {
        onEvent(MainContract.Event.CheckAppState)
    }

    override fun createInitialState(): MainContract.State = MainContract.State()

    override fun onEvent(event: MainContract.Event) {
        when (event) {
            MainContract.Event.CheckAppState -> loadSettings()
            MainContract.Event.AuthSuccess -> updateState { it.copy(isAuthenticated = true) }
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            getOnboardingStatusUseCase().collect { completed ->
                updateState {
                    it.copy(
                        hasSeenOnboarding = completed,
                        isLoading = false
                    )
                }
            }
        }
    }
}