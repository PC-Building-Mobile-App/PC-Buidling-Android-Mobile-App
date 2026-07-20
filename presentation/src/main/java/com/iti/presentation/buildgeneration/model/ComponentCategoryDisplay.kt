package com.iti.presentation.buildgeneration.model

import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.R

val ComponentCategoryType.labelRes: Int
    get() = when (this) {
        ComponentCategoryType.CPU -> R.string.category_cpu
        ComponentCategoryType.MOTHERBOARD -> R.string.category_motherboard
        ComponentCategoryType.GPU -> R.string.category_gpu
        ComponentCategoryType.PSU -> R.string.category_psu
        ComponentCategoryType.CASE -> R.string.category_case
        ComponentCategoryType.COOLER -> R.string.category_cooler
        ComponentCategoryType.MEMORY -> R.string.category_memory
    }