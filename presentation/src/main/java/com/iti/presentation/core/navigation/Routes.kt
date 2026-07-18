package com.iti.presentation.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey

@Serializable
data object OnboardingRoute : Route

@Serializable
data object LoginRoute : Route

@Serializable
data object RegisterRoute : Route

@Serializable
data object HomeRoute : Route

@Serializable
data object PartsRoute : Route

@Serializable
data object AiAssistantRoute : Route

@Serializable
data object MyPcsRoute : Route

@Serializable
data object ProfileRoute : Route

@Serializable
data class PartsDetailRoute(val partId: String) : Route

@Serializable
data class BuildCategoryRoute(val category: String) : Route

@Serializable
data class BuildGenerationRoute(val buildId: String) : Route

@Serializable
data class ComparisonRoute(val firstPartId: String, val secondPartId: String) : Route