package com.iti.presentation.categorybuilds.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.builds.usecase.GetBuildsByCategoryUseCase
import com.iti.presentation.categorybuilds.CategoryBuildsContract.Effect
import com.iti.presentation.categorybuilds.CategoryBuildsContract.Event
import com.iti.presentation.categorybuilds.CategoryBuildsContract.State
import com.iti.presentation.categorybuilds.model.toUiModel
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryBuildsViewModel @Inject constructor(
    private val getBuildsByCategoryUseCase: GetBuildsByCategoryUseCase,
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State(
        isLoading = true,
    )

    init {
        onEvent(Event.LoadBuilds)
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
            is Event.ShareClicked -> {
                val build = state.value.builds.find { it.id == event.buildId }
                if (build != null && build.specs.isNotEmpty()) {
                    sendEffect(Effect.ShareBuild(build))
                }
            }
            is Event.ExportClicked -> {
                val build = state.value.builds.find { it.id == event.buildId }
                if (build != null && build.specs.isNotEmpty()) {
                    sendEffect(Effect.ExportBuild(build))
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