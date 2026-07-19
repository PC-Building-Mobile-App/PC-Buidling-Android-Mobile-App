package com.iti.data.hardwarenews.repository

import com.iti.data.hardwarenews.datasource.HardwareNewsRemoteDataSource
import com.iti.data.hardwarenews.mapper.toDomain
import com.iti.domain.hardwarenews.model.HardwareNewsArticle
import com.iti.domain.hardwarenews.repository.HardwareNewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HardwareNewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: HardwareNewsRemoteDataSource
) : HardwareNewsRepository {

    override fun getLatestHardwareNews(): Flow<List<HardwareNewsArticle>> = flow {
        remoteDataSource.fetchHardwareNews().fold(
            onSuccess = { remoteArticles ->
                emit(remoteArticles.toDomain())
            },
            onFailure = { exception ->
                throw exception
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun getArticleById(id: String): Flow<HardwareNewsArticle?> = flow {
        remoteDataSource.fetchArticleById(id).fold(
            onSuccess = { remoteArticle ->
                emit(remoteArticle?.toDomain())
            },
            onFailure = { exception ->
                throw exception
            }
        )
    }.flowOn(Dispatchers.IO)
}