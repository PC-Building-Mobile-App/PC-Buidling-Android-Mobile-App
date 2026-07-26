package com.iti.presentation.categorybuilds.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.builds.usecase.CompareBuildsUseCase
import com.iti.domain.builds.usecase.GetBuildByIdUseCase
import com.iti.presentation.categorybuilds.ComparisonContract.Effect
import com.iti.presentation.categorybuilds.ComparisonContract.Event
import com.iti.presentation.categorybuilds.ComparisonContract.State
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComparisonViewModel @Inject constructor(
    private val getBuildByIdUseCase: GetBuildByIdUseCase,
    private val compareBuildsUseCase: CompareBuildsUseCase
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State()

    override fun onEvent(event: Event) {
        when (event) {
            is Event.Initialize -> loadData(event.buildIds)
            is Event.TabSelected -> updateState { it.copy(selectedTabIndex = event.index) }
            is Event.BackClicked -> sendEffect(Effect.NavigateBack)
            is Event.ChangeClicked -> sendEffect(Effect.NavigateBack)
            is Event.SwapClicked -> {
                val currentBuilds = state.value.builds
                if (currentBuilds.size >= 2) {
                    updateState { it.copy(builds = currentBuilds.reversed()) }
                }
            }
            is Event.RetryAiClicked -> {
                val builds = state.value.builds
                val ids = builds.mapNotNull { it.id.toIntOrNull() }
                val names = builds.map { it.name }
                if (ids.isNotEmpty()) fetchComparison(ids, names)
            }
        }
    }

    private fun loadData(buildIds: List<Int>) {
        viewModelScope.launch {
            updateState { it.copy(isLoadingDetails = true, isAiLoading = true, errorMessage = null) }

            val detailDeferred = buildIds.map { id ->
                async { getBuildByIdUseCase(id.toString()) }
            }

            val detailsResults = detailDeferred.awaitAll()
            val builds = detailsResults.mapNotNull { it.getOrNull() }

            if (builds.size < buildIds.size) {
                updateState { it.copy(isLoadingDetails = false, errorMessage = detailsResults.find { r -> r.isFailure }?.exceptionOrNull()?.toUiText()) }
            } else {
                updateState { it.copy(isLoadingDetails = false, builds = builds) }
                fetchComparison(builds.mapNotNull { it.id.toIntOrNull() }, builds.map { it.name })
            }
        }
    }

    private fun fetchComparison(buildIds: List<Int>, buildNames: List<String>) {
        viewModelScope.launch {
            updateState { it.copy(isAiLoading = true, aiErrorMessage = null) }
            compareBuildsUseCase(buildIds, buildNames)
                .onSuccess { comparison ->
                    updateState { it.copy(isAiLoading = false, comparison = comparison) }
                }
                .onFailure { throwable ->
                    updateState { it.copy(isAiLoading = false, aiErrorMessage = throwable.toUiText()) }
                }
        }
    }
}
