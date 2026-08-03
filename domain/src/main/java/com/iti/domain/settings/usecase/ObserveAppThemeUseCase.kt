package com.iti.domain.settings.usecase

import com.iti.domain.settings.model.AppThemePreference
import com.iti.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAppThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<AppThemePreference> {
        return settingsRepository.observeThemePreference()
    }
}
