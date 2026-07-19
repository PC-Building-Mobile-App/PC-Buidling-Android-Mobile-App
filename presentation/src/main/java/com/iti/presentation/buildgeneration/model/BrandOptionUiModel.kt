package com.iti.presentation.buildgeneration.model

import com.iti.presentation.R

data class BrandOptionUiModel(
    val labelRes: Int,
    val value: String?,
)

val brandOptions = listOf(
    BrandOptionUiModel(R.string.brand_intel, "Intel"),
    BrandOptionUiModel(R.string.brand_amd, "AMD"),
    BrandOptionUiModel(R.string.brand_nvidia, "NVIDIA"),
    BrandOptionUiModel(R.string.brand_no_preference, null),
)