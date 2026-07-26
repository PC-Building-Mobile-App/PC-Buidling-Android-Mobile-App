package com.iti.presentation.profile.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.auth.usecase.LogoutUseCase
import com.iti.domain.auth.usecase.ObserveCurrentUserUseCase
import com.iti.domain.builds.usecase.GetBuildCategoriesUseCase
import com.iti.domain.locale.usecase.GetAppLanguageUseCase
import com.iti.domain.locale.usecase.SetAppLanguageUseCase
import com.iti.domain.profile.usecase.ObserveAvatarUseCase
import com.iti.domain.profile.usecase.SaveAvatarUseCase
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.profile.ProfileContract.AppLanguage
import com.iti.presentation.profile.ProfileContract.Effect
import com.iti.presentation.profile.ProfileContract.Event
import com.iti.presentation.profile.ProfileContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val getBuildCategoriesUseCase: GetBuildCategoriesUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val observeAvatarUseCase: ObserveAvatarUseCase,
    private val saveAvatarUseCase: SaveAvatarUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<Event, State, Effect>() {

    init {
        observeCurrentUserUseCase()
            .onEach { user ->
                updateState {
                    it.copy(
                        isLoading = false,
                        name = user?.name.orEmpty(),
                        handle = user?.email
                            ?.substringBefore("@")
                            ?.let { local -> "@$local" }
                            .orEmpty(),
                    )
                }
            }
            .launchIn(viewModelScope)

        observeAvatarUseCase()
            .onEach { path -> updateState { it.copy(avatarPath = path) } }
            .launchIn(viewModelScope)

        updateState {
            it.copy(selectedLanguage = currentAppLanguage())
        }

        loadBuildsCount()
    }

    override fun createInitialState(): State = State()

    override fun onEvent(event: Event) {
        when (event) {
            is Event.AvatarPicked -> saveAvatar(event.uriString)
            Event.SavedBuildsClicked -> sendEffect(Effect.NavigateToSavedBuilds)
            Event.LanguageAndRegionClicked -> updateState { it.copy(isLanguageDialogVisible = true) }
            Event.DismissLanguageDialog -> updateState { it.copy(isLanguageDialogVisible = false) }
            is Event.LanguageSelected -> selectLanguage(event.language)
            Event.SignOutClicked -> signOut()
        }
    }

    private fun saveAvatar(uriString: String) {
        viewModelScope.launch {
            saveAvatarUseCase(uriString)

        }
    }

    private fun selectLanguage(language: AppLanguage) {
        val current = state.value
        updateState {
            it.copy(
                selectedLanguage = language,
                isLanguageDialogVisible = false,
            )
        }
        if (language == current.selectedLanguage) return // nothing to do, avoid needless recreate

        setAppLanguageUseCase(language.tag)
        sendEffect(Effect.RecreateActivity)
    }

    private fun currentAppLanguage(): AppLanguage {
        val currentTag = getAppLanguageUseCase()
        return AppLanguage.entries.firstOrNull { it.tag == currentTag } ?: AppLanguage.ENGLISH
    }

    private fun loadBuildsCount() {
        viewModelScope.launch {
            getBuildCategoriesUseCase()
                .onSuccess { categories ->
                    updateState {
                        it.copy(buildsCount = categories.sumOf { category -> category.buildsCount })
                    }
                }

        }
    }

    private fun signOut() {
        viewModelScope.launch {
            logoutUseCase()

        }
    }
}