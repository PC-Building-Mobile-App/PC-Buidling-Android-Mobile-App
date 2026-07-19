package com.iti.presentation.buildgeneration.model

import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.core.UiText

data class ComponentSlotUiModel(
    val category: ComponentCategoryType,
    val component: PickerComponentUiModel? = null,
    val warningMessage: UiText? = null,
)