package com.iti.domain.settings.usecase

import com.iti.domain.settings.model.AppThemePreference
import com.iti.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class SetAppThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(theme: AppThemePreference) {
        settingsRepository.setThemePreference(theme)
    }
}
