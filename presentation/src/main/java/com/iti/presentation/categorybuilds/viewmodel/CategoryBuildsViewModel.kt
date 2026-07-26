package com.iti.presentation.categorybuilds.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.builds.usecase.GetBuildsByCategoryUseCase
import com.iti.presentation.categorybuilds.BuildSelectionManager
import com.iti.presentation.categorybuilds.CategoryBuildsContract.Effect
import com.iti.presentation.categorybuilds.CategoryBuildsContract.Event
import com.iti.presentation.categorybuilds.CategoryBuildsContract.State
import com.iti.presentation.categorybuilds.model.toUiModel
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryBuildsViewModel @Inject constructor(
    private val getBuildsByCategoryUseCase: GetBuildsByCategoryUseCase,
    private val selectionManager: BuildSelectionManager,
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State(
        isLoading = true,
    )

    init {
        onEvent(Event.LoadBuilds)
        observeSelection()
    }

    private fun observeSelection() {
        selectionManager.isSelectionMode
            .onEach { isMode -> updateState { it.copy(isSelectionMode = isMode) } }
            .launchIn(viewModelScope)

        selectionManager.selectedBuilds
            .onEach { selected ->
                updateState { it.copy(selectedBuildIds = selected.map { b -> b.id }.toSet()) }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.Initialize -> {
                updateState {
                    it.copy(category = event.category)
                }
                loadBuilds()
            }

            is Event.LoadBuilds -> loadBuilds()
            is Event.BackClicked -> sendEffect(Effect.NavigateBack)
            is Event.NewBuildClicked -> {
                state.value.category?.let {
                    sendEffect(Effect.NavigateToNewBuild(it))
                }
            }
            is Event.EditClicked -> sendEffect(Effect.NavigateToEditBuild(event.build))
            is Event.ShareClicked -> sendEffect(Effect.ShareBuild(event.buildId))
            is Event.ExportClicked -> sendEffect(Effect.ExportBuild(event.buildId))
            is Event.ToggleSelectionMode -> selectionManager.toggleSelectionMode()
            is Event.BuildSelected -> selectionManager.toggleBuildSelection(event.build)
            is Event.CompareClicked -> {
                val ids = selectionManager.selectedBuilds.value.mapNotNull { it.id.toIntOrNull() }
                if (ids.size >= 2) {
                    sendEffect(Effect.NavigateToComparison(ids))
                }
            }
        }
    }

    private fun loadBuilds() {
        val category = state.value.category ?: return

        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }

            getBuildsByCategoryUseCase(category.id)
                .onSuccess { builds ->
                    updateState { current ->
                        current.copy(isLoading = false, builds = builds.map { it.toUiModel() })
                    }
                }
                .onFailure { throwable ->
                    updateState { current ->
                        current.copy(isLoading = false, errorMessage = throwable.toUiText())
                    }
                }
        }
    }
}