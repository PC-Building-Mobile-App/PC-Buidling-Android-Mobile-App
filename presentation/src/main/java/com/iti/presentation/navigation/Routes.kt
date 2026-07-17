package com.iti.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object OnboardingRoute

@Serializable
data object LoginRoute

@Serializable
data object RegisterRoute

@Serializable
data object HomeRoute

@Serializable
data object PartsRoute

@Serializable
data object AiAssistantRoute

@Serializable
data object MyPcsRoute

@Serializable
data object ProfileRoute


@Serializable
data class PartsDetailRoute(val partId: String)

@Serializable
data class BuildCategoryRoute(val category: String)

@Serializable
data class BuildGenerationRoute(val buildId: String)

@Serializable
data class ComparisonRoute(val firstPartId: String, val secondPartId: String)
