package com.iti.presentation.partdetails

import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.core.pccomponents.model.ComponentUiModel

object PartDetailsContract {
    data class State(
        val component: ComponentUiModel? = null,
        val specs: Map<String, String> = emptyMap(),
        val isAiLoading: Boolean = false,
        val aiExplanation: String? = null,

        // Drawer and Build addition state
        val isDrawerOpen: Boolean = false,
        val builds: List<Build> = emptyList(),
        val isBuildsLoading: Boolean = false,
        val buildsError: String? = null,
        val selectedBuildId: String? = null,
        val isCheckingCompatibility: Boolean = false,
        val isAddingToBuild: Boolean = false,
        val compatibilityError: String? = null,
        val isCreatingNewBuild: Boolean = false,
        val newBuildName: String = "",
        val selectedCategory: BuildCategoryType = BuildCategoryType.GAMING,
    )

    sealed interface Event {
        data object BackClicked : Event
        data object AddToBuildClicked : Event
        data object DismissDrawer : Event
        data class BuildSelected(val build: Build) : Event
        data object CreateNewBuildClicked : Event
        data class NewBuildNameChanged(val name: String) : Event
        data class CategorySelected(val category: BuildCategoryType) : Event
        data object ConfirmNewBuild : Event
        data object ClearCompatibilityError : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class ShowToast(val message: String) : Effect
        data class NavigateToBuildGeneration(val component: ComponentUiModel) : Effect
    }
}
