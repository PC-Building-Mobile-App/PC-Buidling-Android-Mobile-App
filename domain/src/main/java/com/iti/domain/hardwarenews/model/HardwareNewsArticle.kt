package com.iti.domain.hardwarenews.model

data class HardwareNewsArticle(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val author: String,
    val imageUrl: String?,
    val publishedAt: String,
    val sourceName: String?
)