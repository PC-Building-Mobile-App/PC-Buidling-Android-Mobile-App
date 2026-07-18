package com.iti.data.hardwarenews.datasource

import com.iti.data.hardwarenews.model.ArticleDataModel

interface HardwareNewsRemoteDataSource {
    suspend fun fetchHardwareNews(): List<ArticleDataModel>
    suspend fun fetchArticleById(id: String): ArticleDataModel?
}