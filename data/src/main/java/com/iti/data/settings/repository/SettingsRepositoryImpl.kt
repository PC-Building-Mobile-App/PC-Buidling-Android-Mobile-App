package com.iti.data.settings.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iti.domain.settings.model.AppThemePreference
import com.iti.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object Keys {
        val THEME_PREF = stringPreferencesKey("app_theme_pref")
    }

    override fun observeThemePreference(): Flow<AppThemePreference> {
        return dataStore.data.map { prefs ->
            val themeString = prefs[Keys.THEME_PREF]
            // Default to DARK as requested for the iOS parity
            if (themeString == null) {
                AppThemePreference.DARK
            } else {
                try {
                    AppThemePreference.valueOf(themeString)
                } catch (e: IllegalArgumentException) {
                    AppThemePreference.DARK
                }
            }
        }
    }

    override suspend fun setThemePreference(theme: AppThemePreference) {
        dataStore.edit { prefs ->
            prefs[Keys.THEME_PREF] = theme.name
        }
    }
}
