package com.iti.presentation.builds

import com.iti.presentation.builds.model.BuildCategoryUiModel
import com.iti.presentation.core.UiText

object MyPcsContract {

    data class State(
        val isLoading: Boolean = false,
        val categories: List<BuildCategoryUiModel> = emptyList(),
        val errorMessage: UiText? = null
    )

    sealed interface Event {
        data object LoadBuildCategories : Event
        data class CategoryClicked(val categoryId: String) : Event
        data object NewBuildClicked : Event
    }

    sealed interface Effect {
        data class NavigateToCategory(val categoryId: String) : Effect
        data object NavigateToNewBuild : Effect
    }
}