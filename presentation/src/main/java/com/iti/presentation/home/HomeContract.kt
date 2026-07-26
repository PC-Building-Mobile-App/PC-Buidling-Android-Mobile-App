package com.iti.presentation.home

import com.iti.presentation.core.UiText
import com.iti.presentation.core.componentcategories.model.ComponentCategoryUiModel
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.home.model.HardwareNewsUiModel
import com.iti.presentation.home.model.PlatformStatUiModel

object HomeContract {

    data class State(
        val isLoading: Boolean = true,
        val userName: String = "Mock User",
        val searchQuery: String = "",
        val stats: List<PlatformStatUiModel> = emptyList(),
        val featuredComponents: List<ComponentUiModel> = emptyList(),
        val categories: List<ComponentCategoryUiModel> = emptyList(),
        val isCategoriesExpanded: Boolean = false,
        val latestNews: List<HardwareNewsUiModel> = emptyList(),
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
        data class ComponentClicked(val component: ComponentUiModel) : Event
    }

    sealed interface Effect {
        data class NavigateToPartsWithQuery(val query: String) : Effect
        data object NavigateToParts : Effect
        data object NavigateToGenerateBuild : Effect
        data class NavigateToComponentDetail(val componentJson: String) : Effect
        data class NavigateToPartsWithCategory(val categoryId: String) : Effect
        data object NavigateToHardwareNews : Effect
        data class NavigateToNewsDetail(val articleId: String) : Effect
    }
}