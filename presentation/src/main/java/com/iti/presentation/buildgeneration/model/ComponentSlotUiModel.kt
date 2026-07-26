package com.iti.presentation.buildgeneration.model

import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.categorybuilds.model.AlternativeOptionUiModel
import com.iti.presentation.core.UiText
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.core.pccomponents.model.priceValue

data class ComponentSlotUiModel(
    val category: ComponentCategoryType,
    val component: ComponentUiModel? = null,
    val warningMessage: UiText? = null,
    val alternatives: List<AlternativeOptionUiModel> = emptyList(),
)


val List<ComponentUiModel>.totalPrice: Double
    get() = sumOf { it.priceValue }