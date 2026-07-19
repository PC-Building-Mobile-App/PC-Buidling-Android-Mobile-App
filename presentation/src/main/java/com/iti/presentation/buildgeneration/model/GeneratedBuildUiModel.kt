package com.iti.presentation.buildgeneration.model

import com.iti.domain.builds.model.GeneratedBuild

data class GeneratedBuildUiModel(
    val components: List<PickerComponentUiModel>,
    val isCompatible: Boolean,
    val issueMessages: List<String>,
    val warningMessages: List<String>,
)

fun GeneratedBuild.toUiModel(): GeneratedBuildUiModel = GeneratedBuildUiModel(
    components = components.map { it.toPickerUiModel() },
    isCompatible = compatibilityReport.compatible,
    issueMessages = compatibilityReport.issues.map { it.message },
    warningMessages = compatibilityReport.warnings.map { it.message },
)