package com.iti.data.hardwarenews.datasource

import com.iti.data.hardwarenews.model.ArticleDataModel
import com.iti.data.hardwarenews.remote.HardwareNewsApiService
import com.iti.data.util.safeCall
import com.iti.domain.exceptions.ServerException
import javax.inject.Inject

class HardwareNewsRemoteDataSourceImpl @Inject constructor(
    private val apiService: HardwareNewsApiService
) : HardwareNewsRemoteDataSource {

    override suspend fun fetchHardwareNews(): Result<List<ArticleDataModel>> = safeCall {
        val response = apiService.searchHardwareNews()
        if (response.status.equals("ok", ignoreCase = true)) {
            response.news
        } else {
            throw ServerException.Generic(
                message = "API returned status: ${response.status}",
                code = 400
            )
        }
    }

    override suspend fun fetchArticleById(id: String): Result<ArticleDataModel?> = safeCall {
        apiService.getArticleById(id)
    }
}