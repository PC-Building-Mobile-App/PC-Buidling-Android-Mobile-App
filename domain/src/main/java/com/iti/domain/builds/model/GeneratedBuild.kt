package com.iti.domain.builds.model

import com.iti.domain.components.model.Component

data class GeneratedBuild(
    val components: List<Component>,
    val totalPrice: Double,
    val compatibilityReport: CompatibilityReport,
)