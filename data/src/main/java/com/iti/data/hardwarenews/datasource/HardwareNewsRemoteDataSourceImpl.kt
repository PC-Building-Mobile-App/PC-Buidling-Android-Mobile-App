package com.iti.data.hardwarenews.datasource

import com.iti.data.hardwarenews.model.ArticleDataModel
import com.iti.data.hardwarenews.remote.HardwareNewsApiService
import javax.inject.Inject

class HardwareNewsRemoteDataSourceImpl @Inject constructor(
    private val apiService: HardwareNewsApiService
) : HardwareNewsRemoteDataSource {

    override suspend fun fetchHardwareNews(): List<ArticleDataModel> {
        val response = apiService.searchHardwareNews()
        return if (response.status.equals("ok", ignoreCase = true)) {
            response.news
        } else {
            emptyList()
        }
    }

    override suspend fun fetchArticleById(id: String): ArticleDataModel? {
        return apiService.getArticleById(id)
    }
}