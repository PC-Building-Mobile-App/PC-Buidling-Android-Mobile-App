package com.iti.presentation.partdetails

import androidx.lifecycle.viewModelScope
import com.iti.domain.ai.usecase.GetAiOverviewUseCase
import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.CompatibilityCheckRequest
import com.iti.domain.builds.model.CompatibilityCheckTarget
import com.iti.domain.builds.model.CompatibilityMode
import com.iti.domain.builds.model.SaveBuildRequest
import com.iti.domain.builds.usecase.CheckComponentCompatibilityUseCase
import com.iti.domain.builds.usecase.GetAllBuildsUseCase
import com.iti.domain.builds.usecase.SaveBuildUseCase
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartDetailsViewModel @Inject constructor(
    private val getAiOverviewUseCase: GetAiOverviewUseCase,
    private val getAllBuildsUseCase: GetAllBuildsUseCase,
    private val checkComponentCompatibilityUseCase: CheckComponentCompatibilityUseCase,
    private val saveBuildUseCase: SaveBuildUseCase,
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
                updateState { it.copy(isDrawerOpen = true, isBuildsLoading = true, compatibilityError = null) }
                loadBuilds()
            }
            is PartDetailsContract.Event.DismissDrawer -> {
                updateState {
                    it.copy(
                        isDrawerOpen = false,
                        compatibilityError = null,
                        isCreatingNewBuild = false,
                        selectedBuildId = null
                    )
                }
            }
            is PartDetailsContract.Event.BuildSelected -> onBuildSelected(event.build)
            is PartDetailsContract.Event.CreateNewBuildClicked -> {
                updateState { it.copy(isCreatingNewBuild = true, compatibilityError = null) }
            }
            is PartDetailsContract.Event.NewBuildNameChanged -> {
                updateState { it.copy(newBuildName = event.name) }
            }
            is PartDetailsContract.Event.CategorySelected -> {
                updateState { it.copy(selectedCategory = event.category) }
            }
            is PartDetailsContract.Event.ConfirmNewBuild -> onConfirmNewBuild()
            is PartDetailsContract.Event.ClearCompatibilityError -> {
                updateState { it.copy(compatibilityError = null) }
            }
            is PartDetailsContract.Event.BackClicked -> sendEffect(PartDetailsContract.Effect.NavigateBack)
        }
    }

    private fun loadBuilds() {
        viewModelScope.launch {
            updateState { it.copy(isBuildsLoading = true, buildsError = null) }
            getAllBuildsUseCase().fold(
                onSuccess = { list ->
                    updateState { it.copy(builds = list, isBuildsLoading = false) }
                },
                onFailure = { err ->
                    updateState {
                        it.copy(
                            isBuildsLoading = false,
                            buildsError = err.message ?: "Failed to load builds."
                        )
                    }
                }
            )
        }
    }

    private fun onBuildSelected(build: Build) {
        val candidate = state.value.component ?: return
        updateState {
            it.copy(
                selectedBuildId = build.id,
                isCheckingCompatibility = true,
                compatibilityError = null
            )
        }

        viewModelScope.launch {
            val singleSlotCategories = setOf("CPU", "MOTHERBOARD", "PSU", "CASE", "COOLER")
            val candidateCatName = candidate.category.name.uppercase()

            // If component is single-slot and build already has one, prepare to replace it
            val isSingleSlot = candidateCatName in singleSlotCategories
            val remainingItems = if (isSingleSlot) {
                build.items.filter { it.category.name.uppercase() != candidateCatName }
            } else {
                build.items
            }

            val target = CompatibilityCheckTarget.InProgressSelection(
                existingComponentIds = remainingItems.map { it.id }
            )
            val request = CompatibilityCheckRequest(
                target = target,
                candidateComponentId = candidate.id,
                mode = CompatibilityMode.RULE_BASED
            )

            checkComponentCompatibilityUseCase(request).fold(
                onSuccess = { report ->
                    if (!report.compatible) {
                        val issueMsg = report.issues.firstOrNull()?.message
                            ?: "This item is not compatible with '${build.name}'."
                        updateState {
                            it.copy(
                                isCheckingCompatibility = false,
                                selectedBuildId = null,
                                compatibilityError = issueMsg
                            )
                        }
                    } else {
                        // Compatible! Proceed to save updated component list
                        updateState { it.copy(isCheckingCompatibility = false, isAddingToBuild = true) }
                        val updatedItemIds = remainingItems.map { it.id } + candidate.id
                        val saveReq = SaveBuildRequest(
                            name = build.name,
                            componentIds = updatedItemIds,
                            categoryId = build.category.name,
                            buildId = build.id
                        )

                        saveBuildUseCase(saveReq).fold(
                            onSuccess = {
                                val actionText = if (isSingleSlot && build.items.any { it.category.name.uppercase() == candidateCatName }) {
                                    "Replaced $candidateCatName in '${build.name}' with ${candidate.productName}!"
                                } else {
                                    "Added ${candidate.productName} to '${build.name}' successfully!"
                                }
                                updateState {
                                    it.copy(
                                        isAddingToBuild = false,
                                        isDrawerOpen = false,
                                        selectedBuildId = null
                                    )
                                }
                                sendEffect(PartDetailsContract.Effect.ShowToast(actionText))
                            },
                            onFailure = { err ->
                                updateState {
                                    it.copy(
                                        isAddingToBuild = false,
                                        selectedBuildId = null,
                                        compatibilityError = err.message ?: "Failed to update build."
                                    )
                                }
                            }
                        )
                    }
                },
                onFailure = { err ->
                    updateState {
                        it.copy(
                            isCheckingCompatibility = false,
                            selectedBuildId = null,
                            compatibilityError = err.message ?: "Failed to check compatibility."
                        )
                    }
                }
            )
        }
    }

    private fun onConfirmNewBuild() {
        val candidate = state.value.component ?: return
        val buildName = state.value.newBuildName.ifBlank { "New PC Build" }
        val category = state.value.selectedCategory

        updateState {
            it.copy(
                isCheckingCompatibility = true,
                compatibilityError = null
            )
        }

        viewModelScope.launch {
            val target = CompatibilityCheckTarget.InProgressSelection(
                existingComponentIds = emptyList()
            )
            val request = CompatibilityCheckRequest(
                target = target,
                candidateComponentId = candidate.id,
                mode = CompatibilityMode.RULE_BASED
            )

            checkComponentCompatibilityUseCase(request).fold(
                onSuccess = { report ->
                    if (!report.compatible) {
                        val issueMsg = report.issues.firstOrNull()?.message
                            ?: "This item is not compatible."
                        updateState {
                            it.copy(
                                isCheckingCompatibility = false,
                                compatibilityError = issueMsg
                            )
                        }
                    } else {
                        updateState { it.copy(isCheckingCompatibility = false, isAddingToBuild = true) }
                        val saveReq = SaveBuildRequest(
                            name = buildName,
                            componentIds = listOf(candidate.id),
                            categoryId = category.name
                        )
                        saveBuildUseCase(saveReq).fold(
                            onSuccess = {
                                updateState {
                                    it.copy(
                                        isAddingToBuild = false,
                                        isDrawerOpen = false,
                                        isCreatingNewBuild = false,
                                        newBuildName = ""
                                    )
                                }
                                sendEffect(
                                    PartDetailsContract.Effect.ShowToast(
                                        "Created build '$buildName' with ${candidate.productName}!"
                                    )
                                )
                            },
                            onFailure = { err ->
                                updateState {
                                    it.copy(
                                        isAddingToBuild = false,
                                        compatibilityError = err.message ?: "Failed to create build."
                                    )
                                }
                            }
                        )
                    }
                },
                onFailure = { err ->
                    updateState {
                        it.copy(
                            isCheckingCompatibility = false,
                            compatibilityError = err.message ?: "Failed to check compatibility."
                        )
                    }
                }
            )
        }
    }
}
