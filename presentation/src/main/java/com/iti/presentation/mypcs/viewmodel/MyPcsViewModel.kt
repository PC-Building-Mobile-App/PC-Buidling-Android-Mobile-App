package com.iti.presentation.mypcs.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.builds.usecase.GetBuildCategoriesUseCase
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.mypcs.MyPcsContract.Effect
import com.iti.presentation.mypcs.MyPcsContract.Event
import com.iti.presentation.mypcs.MyPcsContract.State
import com.iti.presentation.mypcs.model.toUiModel
import com.iti.presentation.core.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPcsViewModel @Inject constructor(
    private val getBuildCategoriesUseCase: GetBuildCategoriesUseCase,
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State(isLoading = true)

    init {
        onEvent(Event.LoadBuildCategories)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.LoadBuildCategories -> loadBuildCategories()
            is Event.CategoryClicked -> sendEffect(Effect.NavigateToCategory(event.categoryId))
            is Event.NewBuildClicked -> sendEffect(Effect.NavigateToNewBuild)
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
