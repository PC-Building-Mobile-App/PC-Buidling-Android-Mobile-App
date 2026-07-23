package com.iti.presentation.home.model

data class HardwareNewsUiModel(
    val id: String,
    val title: String,
    val description: String,
    val sourceBadge: String,
    val imageUrl: String,
    val articleUrl: String,
    val publishedDate: String
)