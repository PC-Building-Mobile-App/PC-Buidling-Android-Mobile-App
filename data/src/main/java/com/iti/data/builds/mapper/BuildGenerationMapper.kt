package com.iti.data.builds.mapper

import com.iti.data.builds.model.CompatibilityCheckRequestDto
import com.iti.data.builds.model.CompatibilityIssueDto
import com.iti.data.builds.model.CompatibilityReportDto
import com.iti.data.builds.model.GenerateBuildRequestDto
import com.iti.data.builds.model.GeneratedBuildDto
import com.iti.data.builds.model.SaveBuildRequestDto
import com.iti.data.components.mapper.toDomain
import com.iti.domain.builds.model.CompatibilityCheckRequest
import com.iti.domain.builds.model.CompatibilityCheckTarget
import com.iti.domain.builds.model.CompatibilityIssue
import com.iti.domain.builds.model.CompatibilityReport
import com.iti.domain.builds.model.GenerateBuildRequest
import com.iti.domain.builds.model.GeneratedBuild
import com.iti.domain.builds.model.SaveBuildRequest

fun GenerateBuildRequest.toDto(): GenerateBuildRequestDto = GenerateBuildRequestDto(
    budget = budget,
    purpose = purpose.map { it.name },
    brandPreference = brandPreference,
    mode = mode.name,
    existingComponentIds = existingComponentIds,
)

fun CompatibilityReportDto.toDomain(): CompatibilityReport = CompatibilityReport(
    compatible = compatible,
    issues = issues.map { it.toDomain() },
    warnings = warnings.map { it.toDomain() },
)

fun CompatibilityIssueDto.toDomain(): CompatibilityIssue =
    CompatibilityIssue(rule = rule, message = message)

fun CompatibilityCheckRequest.toDto(): CompatibilityCheckRequestDto = CompatibilityCheckRequestDto(
    buildId = (target as? CompatibilityCheckTarget.SavedBuild)?.buildId,
    existingComponentIds = (target as? CompatibilityCheckTarget.InProgressSelection)?.existingComponentIds,
    candidateComponentId = candidateComponentId,
    mode = mode.name,
)

fun SaveBuildRequest.toDto(): SaveBuildRequestDto = SaveBuildRequestDto(
    name = name,
    componentIds = componentIds,
    categoryId = categoryId,
    buildId = buildId

)

fun GeneratedBuildDto.toDomain(
): GeneratedBuild = GeneratedBuild(
    components = components.mapNotNull { it.toDomain() },
    totalPrice = totalPrice,
    compatibilityReport = compatibilityReport.toDomain(),
)