package com.iti.presentation.buildgeneration

import com.iti.domain.builds.model.BuildCategoryType
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.buildgeneration.model.ComponentSlotUiModel
import com.iti.presentation.buildgeneration.model.GeneratedBuildUiModel
import com.iti.presentation.buildgeneration.model.PickerComponentUiModel
import com.iti.presentation.core.UiText
import com.iti.presentation.mypcs.model.BuildCategoryUiModel

object BuildGenerationContract {

    const val MIN_BUDGET = 5_000f
    const val MAX_BUDGET = 150_000f
    const val DEFAULT_BUDGET = 65_000f

    data class State(
        val category: BuildCategoryUiModel? = null,
        val categories: List<BuildCategoryUiModel> = emptyList(),
        val isCategoriesLoading: Boolean = false,
        val slots: List<ComponentSlotUiModel> = ComponentCategoryType.entries.map { ComponentSlotUiModel(category = it) },
        val budget: Float = DEFAULT_BUDGET,
        val selectedCategoryTypes: Set<BuildCategoryType> = emptySet(),
        val selectedBrands: Set<String> = emptySet(),
        val isGenerating: Boolean = false,
        val generatedBuild: GeneratedBuildUiModel? = null,
        val isPickerVisible: Boolean = false,
        val pickerCategory: ComponentCategoryType? = null,
        val pickerComponents: List<PickerComponentUiModel> = emptyList(),
        val isPickerLoading: Boolean = false,
        val isSaveDialogVisible: Boolean = false,
        val buildName: String = "",
        val isSaving: Boolean = false,
        val errorMessage: UiText? = null,
    ) {
        val filledSlotsCount: Int get() = slots.count { it.component != null }
        val allSlotsFilled: Boolean get() = slots.all { it.component != null }
    }

    sealed interface Event {
        data class Initialize(val category: BuildCategoryUiModel?) : Event
        data class BudgetChanged(val budget: Float) : Event
        data class CategoryTypeToggled(val type: BuildCategoryType) : Event
        data class BrandToggled(val brand: String?) : Event
        data class SlotClicked(val category: ComponentCategoryType) : Event
        data object PickerDismissed : Event
        data class ComponentPicked(val component: PickerComponentUiModel) : Event
        data class SlotCleared(val category: ComponentCategoryType) : Event
        data object GenerateClicked : Event
        data object SaveClicked : Event
        data class BuildNameChanged(val name: String) : Event
        data object ConfirmSaveClicked : Event
        data object SaveDialogDismissed : Event
        data object BackClicked : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class ShowMessage(val message: UiText, val isError: Boolean = true) : Effect
    }
}