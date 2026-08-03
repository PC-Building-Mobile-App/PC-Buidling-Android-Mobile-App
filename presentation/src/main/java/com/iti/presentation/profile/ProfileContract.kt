package com.iti.presentation.profile

import com.iti.domain.settings.model.AppThemePreference

object ProfileContract {

    enum class AppLanguage(val tag: String, val displayName: String) {
        ENGLISH("en", "English"),
        ARABIC("ar", "العربية"),
    }

    data class State(
        val isLoading: Boolean = true,
        val name: String = "",
        val handle: String = "",
        val avatarPath: String? = null,
        val buildsCount: Int = 0,
        val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
        val isLanguageDialogVisible: Boolean = false,
        val selectedTheme: AppThemePreference = AppThemePreference.DARK,
        val isThemeDialogVisible: Boolean = false,
    )

    sealed interface Event {
        data class AvatarPicked(val uriString: String) : Event
        data object SavedBuildsClicked : Event
        data object LanguageAndRegionClicked : Event
        data object DismissLanguageDialog : Event
        data class LanguageSelected(val language: AppLanguage) : Event
        data object ThemeClicked : Event
        data object DismissThemeDialog : Event
        data class ThemeSelected(val theme: AppThemePreference) : Event
        data object SignOutClicked : Event
    }

    sealed interface Effect {
        data object NavigateToSavedBuilds : Effect
        data object RecreateActivity : Effect

    }
}