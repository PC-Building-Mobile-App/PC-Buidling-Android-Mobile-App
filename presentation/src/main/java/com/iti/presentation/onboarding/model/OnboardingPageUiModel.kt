package com.iti.presentation.onboarding.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Brush

data class OnboardingPageUiModel(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String,
    val buttonGradient: Brush
)