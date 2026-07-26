package com.iti.presentation.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.auth.usecase.ObserveCurrentUserUseCase
import com.iti.domain.componentcategories.usecase.GetComponentCategoriesUseCase
import com.iti.domain.components.usecase.GetRandomComponentsUseCase
import com.iti.domain.hardwarenews.usecase.GetLatestHardwareNewsUseCase
import com.iti.domain.stats.usecase.GetPlatformStatsUseCase
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.toUiText
import com.iti.presentation.core.componentcategories.mapper.toUiModels
import com.iti.presentation.core.pccomponents.mapper.toUiModels
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.home.HomeContract.Effect
import com.iti.presentation.home.HomeContract.Event
import com.iti.presentation.home.HomeContract.State
import com.iti.presentation.home.mapper.toUiModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRandomComponentsUseCase: GetRandomComponentsUseCase,
    private val getCategoriesUseCase: GetComponentCategoriesUseCase,
    private val getLatestNewsUseCase: GetLatestHardwareNewsUseCase,
    private val getPlatformStatsUseCase: GetPlatformStatsUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State()

    init {
        loadHomeData()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.UpdateSearchQuery -> updateState { it.copy(searchQuery = event.query) }
            is Event.SearchSubmitted -> {
                val query = state.value.searchQuery.trim()
                if (query.isNotEmpty()) sendEffect(Effect.NavigateToPartsWithQuery(query))
            }
            is Event.GenerateBuildClicked -> sendEffect(Effect.NavigateToGenerateBuild)
            is Event.SeeAllComponentsClicked -> sendEffect(Effect.NavigateToParts)
            is Event.ToggleCategoriesExpanded -> updateState { it.copy(isCategoriesExpanded = !it.isCategoriesExpanded) }
            is Event.CategoryClicked -> sendEffect(Effect.NavigateToPartsWithCategory(event.category.id))
            is Event.SeeAllNewsClicked -> sendEffect(Effect.NavigateToHardwareNews)
            is Event.NewsClicked -> sendEffect(Effect.NavigateToNewsDetail(event.newsId))
            is Event.ComponentClicked -> {
                val json = Json.encodeToString(ComponentUiModel.serializer(), event.component)
                sendEffect(Effect.NavigateToComponentDetail(json))
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            val componentsFlow = getRandomComponentsUseCase().catch { emit(emptyList()) }
            val categoriesFlow = getCategoriesUseCase().catch { emit(emptyList()) }
            val newsFlow = getLatestNewsUseCase(limit = 10).catch { emit(emptyList()) }
            val statsFlow = getPlatformStatsUseCase().catch { emit(emptyList()) }
            val userFlow = observeCurrentUserUseCase().catch { emit(null) }
            combine(
                componentsFlow,
                categoriesFlow,
                newsFlow,
                statsFlow,
                userFlow
            ) { components, categories, news, stats, user ->
                State(
                    isLoading = false,
                    userName = user?.name ?: state.value.userName,
                    searchQuery = state.value.searchQuery,
                    isCategoriesExpanded = state.value.isCategoriesExpanded,
                    featuredComponents = components.toUiModels(),
                    categories = categories.toUiModels(),
                    latestNews = news.toUiModels(),
                    stats = stats.toUiModels(),
                    errorMessage = null
                )
            }.catch { throwable ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.toUiText()
                    )
                }
            }.collect { newState ->
                updateState { newState }
            }
        }
    }
}