package com.iti.presentation.onboarding

import androidx.lifecycle.viewModelScope
import com.iti.domain.onboarding.usecase.CompleteOnboardingUseCase
import com.iti.domain.onboarding.usecase.GetOnboardingStatusUseCase
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : BaseViewModel<OnboardingContract.Event, OnboardingContract.State, OnboardingContract.Effect>() {

    init {
        observeOnboardingStatus()
    }

    override fun createInitialState(): OnboardingContract.State = OnboardingContract.State()

    override fun onEvent(event: OnboardingContract.Event) {
        when (event) {
            OnboardingContract.Event.CompleteOnboarding -> handleCompleteOnboarding()
        }
    }

    private fun observeOnboardingStatus() {
        viewModelScope.launch {
            getOnboardingStatusUseCase().collect { completed ->
                updateState {
                    it.copy(
                        hasCompletedOnboarding = completed,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun handleCompleteOnboarding() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            try {
                completeOnboardingUseCase()
                sendEffect(OnboardingContract.Effect.NavigateToAuth)
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = UiText.DynamicString(e.message ?: "Unknown Error")
                    )
                }
            }
        }
    }
}