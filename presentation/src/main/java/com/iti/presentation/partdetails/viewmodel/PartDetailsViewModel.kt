package com.iti.presentation.partdetails

import androidx.lifecycle.viewModelScope
import com.iti.domain.ai.usecase.GetAiOverviewUseCase
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PartDetailsViewModel @Inject constructor(
    private val getAiOverviewUseCase: GetAiOverviewUseCase,
) : BaseViewModel<PartDetailsContract.Event, PartDetailsContract.State, PartDetailsContract.Effect>() {

    override fun createInitialState() = PartDetailsContract.State()

    fun setComponent(component: ComponentUiModel) {
        updateState {
            it.copy(
                component = component,
                specs = component.specs,
            )
        }
        fetchAiExplanation(component.productName, component.specs)
    }

    private fun fetchAiExplanation(name: String, specs: Map<String, String>) {
        updateState { it.copy(isAiLoading = true) }

        val prompt = "Explain this PC component briefly to a buyer (max 3 sentences). " +
                "Component: $name. Specs: $specs"

        getAiOverviewUseCase(prompt)
            .onEach { result ->
                result.onSuccess { explanation ->
                    updateState { it.copy(isAiLoading = false, aiExplanation = explanation) }
                }.onFailure {
                    updateState {
                        it.copy(isAiLoading = false, aiExplanation = "AI explanation temporarily unavailable.")
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: PartDetailsContract.Event) {
        when (event) {
            is PartDetailsContract.Event.AddToBuildClicked -> {
                state.value.component?.let {
                    sendEffect(PartDetailsContract.Effect.NavigateToBuildGeneration(it))
                }
            }
            PartDetailsContract.Event.BackClicked -> sendEffect(PartDetailsContract.Effect.NavigateBack)
        }
    }
}