package com.iti.presentation.mypcs

import com.iti.presentation.mypcs.model.BuildCategoryUiModel
import com.iti.presentation.core.UiText

object MyPcsContract {

    data class State(
        val isLoading: Boolean = false,
        val categories: List<BuildCategoryUiModel> = emptyList(),
        val errorMessage: UiText? = null,
        val isSelectionMode: Boolean = false,
    )

    sealed interface Event {
        data object LoadBuildCategories : Event
        data class CategoryClicked(val category: BuildCategoryUiModel) : Event
        data object NewBuildClicked : Event
        data object ToggleSelectionMode : Event
    }

    sealed interface Effect {
        data class NavigateToCategory(val category: BuildCategoryUiModel) : Effect
        data object NavigateToNewBuild : Effect
    }
}