package com.iti.presentation.home

import com.iti.presentation.core.UiText
import com.iti.presentation.core.componentcategories.model.ComponentCategoryUiModel
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.home.model.HardwareNewsUiModel
import com.iti.presentation.home.model.PlatformStatUiModel

object HomeContract {

    data class State(
        val isLoading: Boolean = true,
        // TODO: Replace "Mock User" with actual user name from backend when user profile API is ready
        val userName: String = "Mock User",
        val searchQuery: String = "",
        val stats: List<PlatformStatUiModel> = emptyList<PlatformStatUiModel>(),
        val featuredComponents: List<ComponentUiModel> = emptyList<ComponentUiModel>(),
        val categories: List<ComponentCategoryUiModel> = emptyList<ComponentCategoryUiModel>(),
        val isCategoriesExpanded: Boolean = false,
        val latestNews: List<HardwareNewsUiModel> = emptyList<HardwareNewsUiModel>(),
        val errorMessage: UiText? = null
    )

    sealed interface Event {
        data class UpdateSearchQuery(val query: String) : Event
        data object SearchSubmitted : Event
        data object GenerateBuildClicked : Event
        data object SeeAllComponentsClicked : Event
        data object ToggleCategoriesExpanded : Event
        data class CategoryClicked(val category: ComponentCategoryUiModel) : Event
        data object SeeAllNewsClicked : Event
        data class NewsClicked(val newsId: String) : Event
        data class ComponentClicked(val componentId: Long) : Event
    }

    sealed interface Effect {
        data class NavigateToPartsWithQuery(val query: String) : Effect
        data object NavigateToParts : Effect
        data object NavigateToGenerateBuild : Effect
        data class NavigateToComponentDetail(val componentId: Long) : Effect
        data class NavigateToPartsWithCategory(val categoryId: String) : Effect
        data object NavigateToHardwareNews : Effect
        data class NavigateToNewsDetail(val articleId: String) : Effect
    }
}
