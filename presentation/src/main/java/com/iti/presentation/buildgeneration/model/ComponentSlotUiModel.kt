package com.iti.presentation.buildgeneration.model

import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.core.UiText
import com.iti.presentation.core.pccomponents.model.ComponentUiModel

data class ComponentSlotUiModel(
    val category: ComponentCategoryType,
    val component: ComponentUiModel? = null,
    val warningMessage: UiText? = null,
)