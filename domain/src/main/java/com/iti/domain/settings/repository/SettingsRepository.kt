package com.iti.domain.settings.repository

import com.iti.domain.settings.model.AppThemePreference
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeThemePreference(): Flow<AppThemePreference>
    suspend fun setThemePreference(theme: AppThemePreference)
}
