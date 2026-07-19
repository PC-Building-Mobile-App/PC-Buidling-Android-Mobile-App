package com.iti.presentation.buildgeneration.model

import com.iti.domain.builds.model.GeneratedBuild
import com.iti.presentation.core.components.mapper.toUiModels
import com.iti.presentation.core.components.model.ComponentUiModel

data class GeneratedBuildUiModel(
    val components: List<ComponentUiModel>,
    val isCompatible: Boolean,
    val issueMessages: List<String>,
    val warningMessages: List<String>,
)

fun GeneratedBuild.toUiModel(): GeneratedBuildUiModel = GeneratedBuildUiModel(
    components = components.toUiModels(),
    isCompatible = compatibilityReport.compatible,
    issueMessages = compatibilityReport.issues.map { it.message },
    warningMessages = compatibilityReport.warnings.map { it.message },
)