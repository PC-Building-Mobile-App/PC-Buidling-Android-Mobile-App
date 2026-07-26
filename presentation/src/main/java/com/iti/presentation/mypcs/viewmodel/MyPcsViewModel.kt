package com.iti.presentation.mypcs.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.builds.usecase.GetBuildCategoriesUseCase
import com.iti.presentation.categorybuilds.BuildSelectionManager
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.mypcs.MyPcsContract.Effect
import com.iti.presentation.mypcs.MyPcsContract.Event
import com.iti.presentation.mypcs.MyPcsContract.State
import com.iti.presentation.mypcs.model.toUiModel
import com.iti.presentation.core.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPcsViewModel @Inject constructor(
    private val getBuildCategoriesUseCase: GetBuildCategoriesUseCase,
    private val selectionManager: BuildSelectionManager,
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State(isLoading = true)

    init {
        onEvent(Event.LoadBuildCategories)
        observeSelectionMode()
    }

    private fun observeSelectionMode() {
        selectionManager.isSelectionMode
            .onEach { isMode -> updateState { it.copy(isSelectionMode = isMode) } }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.LoadBuildCategories -> loadBuildCategories()
            is Event.CategoryClicked -> sendEffect(Effect.NavigateToCategory(event.category))
            is Event.NewBuildClicked -> sendEffect(Effect.NavigateToNewBuild)
            is Event.ToggleSelectionMode -> selectionManager.toggleSelectionMode()
        }
    }

    private fun loadBuildCategories() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }

            getBuildCategoriesUseCase()
                .onSuccess { domainCategories ->
                    updateState { currentState ->
                        currentState.copy(
                            isLoading = false,
                            categories = domainCategories.map { it.toUiModel() }
                        )
                    }
                }
                .onFailure { throwable ->
                    updateState { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = throwable.toUiText()
                        )
                    }
                }
        }
    }
}
