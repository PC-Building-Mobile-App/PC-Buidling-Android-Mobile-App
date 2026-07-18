package com.iti.data.hardwarenews.mapper

import com.iti.data.hardwarenews.model.ArticleDataModel
import com.iti.domain.hardwarenews.model.HardwareNewsArticle
import javax.inject.Inject

class HardwareNewsDataMapper @Inject constructor() {

    fun mapToDomain(dataModel: ArticleDataModel): HardwareNewsArticle {
        return HardwareNewsArticle(
            id = dataModel.id,
            title = dataModel.title,
            description = dataModel.description,
            url = dataModel.url,
            author = dataModel.author.ifBlank { "Unknown Author" },
            imageUrl = dataModel.image ?: dataModel.urlToImage,
            publishedAt = dataModel.published,
            sourceName = dataModel.source?.name ?: "Hardware News"
        )
    }

    fun mapToDomainList(dataModels: List<ArticleDataModel>): List<HardwareNewsArticle> {
        return dataModels.map { mapToDomain(it) }
    }
}