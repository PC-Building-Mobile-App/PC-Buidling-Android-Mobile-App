package com.iti.presentation.buildgeneration.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.domain.builds.model.CompatibilityCheckRequest
import com.iti.domain.builds.model.CompatibilityCheckTarget
import com.iti.domain.builds.model.GenerateBuildRequest
import com.iti.domain.builds.model.SaveBuildRequest
import com.iti.domain.builds.usecase.CheckComponentCompatibilityUseCase
import com.iti.domain.builds.usecase.GenerateBuildUseCase
import com.iti.domain.builds.usecase.GetBuildCategoriesUseCase
import com.iti.domain.builds.usecase.SaveBuildUseCase
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.domain.components.model.Component
import com.iti.domain.components.usecase.GetComponentsByCategoryUseCase
import com.iti.presentation.R
import com.iti.presentation.buildgeneration.BuildGenerationContract.Effect
import com.iti.presentation.buildgeneration.BuildGenerationContract.Event
import com.iti.presentation.buildgeneration.BuildGenerationContract.State
import com.iti.presentation.buildgeneration.model.ComponentSlotUiModel
import com.iti.presentation.buildgeneration.model.toUiModel
import com.iti.presentation.categorybuilds.model.BuildUiModel
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.UiText
import com.iti.presentation.core.pccomponents.mapper.toUiModel
import com.iti.presentation.core.pccomponents.mapper.toUiModels
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.core.toUiText
import com.iti.presentation.mypcs.model.BuildCategoryUiModel
import com.iti.presentation.mypcs.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuildGenerationViewModel @Inject constructor(
    private val generateBuildUseCase: GenerateBuildUseCase,
    private val checkComponentCompatibilityUseCase: CheckComponentCompatibilityUseCase,
    private val saveBuildUseCase: SaveBuildUseCase,
    private val getComponentsByCategoryUseCase: GetComponentsByCategoryUseCase,
    private val getBuildCategoriesUseCase: GetBuildCategoriesUseCase,
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State()

    override fun onEvent(event: Event) {
        when (event) {
            is Event.Initialize -> initialize(event.category, event.editingBuild)
            is Event.BudgetChanged -> updateState { it.copy(budget = event.budget) }
            is Event.CategoryTypeToggled -> toggleCategoryType(event.type)
            is Event.BrandToggled -> toggleBrand(event.brand)
            is Event.SlotClicked -> openPicker(event.category)
            is Event.PickerDismissed -> updateState {
                it.copy(
                    isPickerVisible = false,
                    pickerCategory = null
                )
            }

            is Event.ComponentPicked -> pickComponent(event.component)
            is Event.SlotCleared -> clearSlot(event.category)
            is Event.GenerateClicked -> generateBuild()
            is Event.SaveClicked -> onSave()
            is Event.BuildNameChanged -> updateState { it.copy(buildName = event.name) }
            is Event.ConfirmSaveClicked -> saveBuild()
            is Event.SaveDialogDismissed -> updateState { it.copy(isSaveDialogVisible = false) }
            is Event.BackClicked -> sendEffect(Effect.NavigateBack)
        }
    }

    private fun initialize(category: BuildCategoryUiModel?, editingBuild: BuildUiModel?) {
        updateState {
            it.copy(
                category = category,
                selectedCategoryTypes = category?.type?.let { type -> setOf(type) } ?: emptySet(),
                isEditingExistingBuild = editingBuild != null,
                editingBuildId = editingBuild?.id,
                budget = editingBuild?.totalPrice?.toFloat() ?: it.budget,
                buildName = editingBuild?.name ?: it.buildName,
            )
        }
        if (category == null) {
            loadCategories()
        }
        if (editingBuild != null) {
            resolveSlots(editingBuild.specs)
        }
    }

    private fun resolveSlots(components: List<ComponentUiModel>) {
        updateState { state ->
            val updatedSlots = ComponentCategoryType.entries.map { categoryType ->
                ComponentSlotUiModel(
                    category = categoryType,
                    component = components.find { it.category == categoryType }
                )
            }
            state.copy(slots = updatedSlots)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            updateState { it.copy(isCategoriesLoading = true) }
            getBuildCategoriesUseCase()
                .onSuccess { domainCategories ->
                    val uiModels = domainCategories.map { cat -> cat.toUiModel() }
                    updateState {
                        it.copy(
                            isCategoriesLoading = false,
                            categories = uiModels,
                        )
                    }
                }
                .onFailure { throwable ->
                    updateState { it.copy(isCategoriesLoading = false) }
                    sendEffect(Effect.ShowMessage(throwable.toUiText()))
                }
        }
    }

    private fun toggleCategoryType(type: BuildCategoryType) {
        updateState { current ->
            val updated = current.selectedCategoryTypes.toMutableSet()
            if (!updated.add(type)) updated.remove(type)
            current.copy(selectedCategoryTypes = updated)
        }
    }

    private fun toggleBrand(brand: String?) {
        updateState { current ->
            val updated = if (brand == null) {
                emptySet()
            } else {
                val mutable = current.selectedBrands.toMutableSet()
                if (!mutable.add(brand)) mutable.remove(brand)
                mutable
            }
            current.copy(selectedBrands = updated)
        }
    }

    private fun openPicker(category: ComponentCategoryType) {
        updateState {
            it.copy(
                isPickerVisible = true,
                pickerCategory = category,
                isPickerLoading = true,
                pickerComponents = emptyList(),
            )
        }
        viewModelScope.launch {
            runCatching { getComponentsByCategoryUseCase(category).first() }
                .onSuccess { components ->
                    updateState {
                        it.copy(
                            isPickerLoading = false,
                            pickerComponents = components.toUiModels()
                        )
                    }
                }
                .onFailure { throwable ->
                    updateState { it.copy(isPickerLoading = false, isPickerVisible = false) }
                    sendEffect(Effect.ShowMessage(throwable.toUiText()))
                }
        }
    }

    private fun pickComponent(component: ComponentUiModel) {
        val category = state.value.pickerCategory ?: return
        val existingIds = state.value.slots.mapNotNull { it.component?.id }

        val request = CompatibilityCheckRequest(
            target = CompatibilityCheckTarget.InProgressSelection(existingIds),
            candidateComponentId = component.id,
        )

        viewModelScope.launch {
            updateState { it.copy(isPickerVisible = false, pickerCategory = null) }

            checkComponentCompatibilityUseCase(request)
                .onSuccess { report ->
                    if (!report.compatible) {
                        sendEffect(Effect.ShowMessage(UiText.DynamicString(report.issues.joinToString { it.message })))
                        return@onSuccess
                    }
                    val warning =
                        report.warnings.firstOrNull()?.let { UiText.DynamicString(it.message) }
                    applyComponentToSlot(category, component, warning)
                }
                .onFailure { throwable -> sendEffect(Effect.ShowMessage(throwable.toUiText())) }
        }
    }

    private fun applyComponentToSlot(
        category: ComponentCategoryType,
        component: ComponentUiModel,
        warning: UiText?
    ) {
        updateState { current ->
            current.copy(
                slots = current.slots.map { slot ->
                    if (slot.category == category) slot.copy(
                        component = component,
                        warningMessage = warning
                    ) else slot
                },
            )
        }
    }

    private fun clearSlot(category: ComponentCategoryType) {
        updateState { current ->
            current.copy(
                slots = current.slots.map { slot ->
                    if (slot.category == category) slot.copy(
                        component = null,
                        warningMessage = null
                    ) else slot
                },
                generatedBuild = null,
            )
        }
    }

    private fun generateBuild() {
        val current = state.value
        val existingIds = current.slots.mapNotNull { it.component?.id }

        val request = GenerateBuildRequest.create(
            budget = current.budget.toDouble(),
            purpose = current.selectedCategoryTypes.toList(),
            brandPreference = current.selectedBrands.toList(),
            isEditingExistingBuild = current.isEditingExistingBuild,
            existingComponentIds = existingIds,
        )

        viewModelScope.launch {
            updateState { it.copy(isGenerating = true, errorMessage = null) }

            generateBuildUseCase(request)
                .onSuccess { generatedBuild ->
                    updateState {
                        it.copy(
                            isGenerating = false,
                            generatedBuild = generatedBuild.toUiModel()
                        )
                    }
                    mergeGeneratedComponentsIntoSlots(generatedBuild.components)
                    if (generatedBuild.compatibilityReport.compatible) {
                        sendEffect(
                            Effect.ShowMessage(
                                message = UiText.StringResource(R.string.compatibility_success_message),
                                isError = false
                            )
                        )
                    } else {
                        val issueMessage =
                            generatedBuild.compatibilityReport.issues.firstOrNull()?.message
                        sendEffect(
                            Effect.ShowMessage(
                                message = issueMessage?.let { UiText.DynamicString(it) }
                                    ?: UiText.StringResource(R.string.compatibility_incompatible_parts_message),
                                isError = true
                            )
                        )
                    }
                }
                .onFailure { throwable ->
                    updateState {
                        it.copy(
                            isGenerating = false,
                            errorMessage = throwable.toUiText()
                        )
                    }
                    sendEffect(Effect.ShowMessage(throwable.toUiText()))
                }
        }
    }

    private fun mergeGeneratedComponentsIntoSlots(components: List<Component>) {
        updateState { current ->
            current.copy(
                slots = current.slots.map { slot ->
                    if (slot.component != null) return@map slot
                    val match = components.firstOrNull {
                        it.category.equals(
                            slot.category.name,
                            ignoreCase = true
                        )
                    }
                    if (match != null) slot.copy(component = match.toUiModel()) else slot
                },
            )
        }
    }

    private fun onSave() {
        val current = state.value

        if (!current.allSlotsFilled) {
            sendEffect(
                Effect.ShowMessage(
                    UiText.StringResource(R.string.save_build_incomplete_slots_message)
                )
            )
            return
        }

        val targetCategoryId = current.category?.id
            ?: current.categories.firstOrNull { it.type in current.selectedCategoryTypes }?.id

        if (targetCategoryId == null) {
            sendEffect(Effect.ShowMessage(UiText.StringResource(R.string.save_build_missing_category_message)))
            return
        }

        updateState { it.copy(isSaveDialogVisible = true) }
    }

    private fun saveBuild() {
        val current = state.value

        if (!current.allSlotsFilled) {
            updateState { it.copy(isSaveDialogVisible = false) }
            sendEffect(
                Effect.ShowMessage(
                    UiText.StringResource(R.string.save_build_incomplete_slots_message)
                )
            )
            return
        }

        if (current.buildName.isBlank()) {
            sendEffect(Effect.ShowMessage(UiText.StringResource(R.string.save_build_name_required_message)))
            return
        }

        val targetCategoryId = current.category?.id
            ?: current.categories.firstOrNull { it.type in current.selectedCategoryTypes }?.id

        if (targetCategoryId == null) {
            sendEffect(Effect.ShowMessage(UiText.StringResource(R.string.save_build_missing_category_message)))
            return
        }

        updateState { it.copy(isSaveDialogVisible = false, isSaving = true) }

        val componentIds = current.slots.mapNotNull { it.component?.id }

        val request = SaveBuildRequest(
            buildId = current.editingBuildId,
            name = current.buildName,
            componentIds = componentIds,
            categoryId = targetCategoryId,
        )

        viewModelScope.launch {
            saveBuildUseCase(request)
                .onSuccess {
                    updateState { it.copy(isSaving = false) }
                    sendEffect(Effect.NavigateBack)
                }
                .onFailure { throwable ->
                    updateState {
                        it.copy(
                            isSaving = false,
                            errorMessage = throwable.toUiText()
                        )
                    }
                    sendEffect(Effect.ShowMessage(throwable.toUiText()))
                }
        }
    }
}