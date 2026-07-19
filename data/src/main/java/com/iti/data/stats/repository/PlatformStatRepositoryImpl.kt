package com.iti.data.stats.repository

import com.iti.data.stats.datasource.PlatformStatDataSource
import com.iti.data.stats.mapper.toDomain
import com.iti.domain.stats.model.PlatformStat
import com.iti.domain.stats.repository.PlatformStatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlatformStatRepositoryImpl @Inject constructor(
    private val localDataSource: PlatformStatDataSource
) : PlatformStatRepository {

    override fun getPlatformStats(): Flow<List<PlatformStat>> {
        return localDataSource.getStats().map { dataModels ->
            dataModels.toDomain()
        }
    }
}