package com.iti.presentation.categorybuilds

import com.iti.presentation.categorybuilds.model.BuildUiModel
import com.iti.presentation.core.UiText
import com.iti.presentation.mypcs.model.BuildCategoryUiModel

object CategoryBuildsContract {

    data class State(
        val isLoading: Boolean = false,
        val category: BuildCategoryUiModel? = null,
        val builds: List<BuildUiModel> = emptyList(),
        val errorMessage: UiText? = null,
    ) {
        val isEmpty: Boolean get() = !isLoading && errorMessage == null && builds.isEmpty()
    }

    sealed interface Event {
        data class Initialize(val category: BuildCategoryUiModel) : Event
        data object LoadBuilds : Event
        data object BackClicked : Event
        data object NewBuildClicked : Event
        data class EditClicked(val build: BuildUiModel) : Event
        data class ShareClicked(val buildId: String) : Event
        data class ExportClicked(val buildId: String) : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class NavigateToNewBuild(val category: BuildCategoryUiModel) : Effect
        data class NavigateToEditBuild(val build: BuildUiModel) : Effect
        data class ShareBuild(val build: BuildUiModel) : Effect
        data class ExportBuild(val build: BuildUiModel) : Effect
    }
}