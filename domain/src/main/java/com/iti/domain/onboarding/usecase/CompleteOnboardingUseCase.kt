package com.iti.domain.onboarding.usecase
import com.iti.domain.onboarding.repository.UserPreferencesRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke() {
        repository.saveOnboardingCompletedState(true)
    }
}