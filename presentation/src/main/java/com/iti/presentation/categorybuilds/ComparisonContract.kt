package com.iti.presentation.categorybuilds

import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildComparison
import com.iti.presentation.core.UiText

object ComparisonContract {

    data class State(
        val isLoadingDetails: Boolean = false,
        val isAiLoading: Boolean = false,
        val builds: List<Build> = emptyList(),
        val comparison: BuildComparison? = null,
        val errorMessage: UiText? = null,
        val aiErrorMessage: UiText? = null,
        val selectedTabIndex: Int = 0
    ) {
        val categories: List<String> = listOf("Overview") + 
            builds.flatMap { b -> b.items.map { it.category.name } }.distinct().sorted()
    }

    sealed interface Event {
        data class Initialize(val buildIds: List<Int>) : Event
        data class TabSelected(val index: Int) : Event
        data object BackClicked : Event
        data object RetryAiClicked : Event
        data object SwapClicked : Event
        data object ChangeClicked : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
    }
}
