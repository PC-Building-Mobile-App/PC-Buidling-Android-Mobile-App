package com.iti.data.hardwarenews.repository

import com.iti.data.hardwarenews.datasource.HardwareNewsRemoteDataSource
import com.iti.data.hardwarenews.mapper.HardwareNewsDataMapper
import com.iti.domain.hardwarenews.model.HardwareNewsArticle
import com.iti.domain.hardwarenews.repository.HardwareNewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HardwareNewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: HardwareNewsRemoteDataSource,
    private val mapper: HardwareNewsDataMapper
) : HardwareNewsRepository {

    override fun getLatestHardwareNews(): Flow<List<HardwareNewsArticle>> = flow {
        val remoteArticles = remoteDataSource.fetchHardwareNews()
        emit(mapper.mapToDomainList(remoteArticles))
    }.flowOn(Dispatchers.IO)

    override fun getArticleById(id: String): Flow<HardwareNewsArticle?> = flow {
        val remoteArticle = remoteDataSource.fetchArticleById(id)
        val domainArticle = remoteArticle?.let { mapper.mapToDomain(it) }
        emit(domainArticle)
    }.flowOn(Dispatchers.IO)
}