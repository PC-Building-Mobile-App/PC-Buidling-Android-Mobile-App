package com.iti.domain.onboarding.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val hasCompletedOnboarding: Flow<Boolean>
    suspend fun saveOnboardingCompletedState(completed: Boolean)
}