package com.iti.data.hardwarenews.mapper

import com.iti.data.hardwarenews.model.ArticleDataModel
import com.iti.domain.hardwarenews.model.HardwareNewsArticle

fun ArticleDataModel.toDomain(): HardwareNewsArticle = HardwareNewsArticle(
    id = id,
    title = title,
    description = description,
    url = url,
    author = author.ifBlank { "Unknown Author" },
    imageUrl = image ?: urlToImage,
    publishedAt = published,
    sourceName = source?.name ?: "Hardware News",
)

fun List<ArticleDataModel>.toDomain(): List<HardwareNewsArticle> = map { it.toDomain() }