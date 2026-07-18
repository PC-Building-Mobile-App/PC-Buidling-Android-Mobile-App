package com.iti.data.stats.repository

import com.iti.data.stats.datasource.PlatformStatDataSource
import com.iti.data.stats.mapper.PlatformStatDataMapper
import com.iti.domain.stats.model.PlatformStat
import com.iti.domain.stats.repository.PlatformStatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlatformStatRepositoryImpl @Inject constructor(
    private val localDataSource: PlatformStatDataSource,
    private val mapper: PlatformStatDataMapper
) : PlatformStatRepository {

    override fun getPlatformStats(): Flow<List<PlatformStat>> {
        return localDataSource.getStats().map { dataModels ->
            mapper.mapToDomainList(dataModels)
        }
    }
}