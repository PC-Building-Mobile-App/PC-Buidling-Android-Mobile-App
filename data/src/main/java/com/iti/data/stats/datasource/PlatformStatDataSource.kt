package com.iti.data.stats.datasource

import com.iti.data.stats.model.PlatformStatDataModel
import kotlinx.coroutines.flow.Flow

interface PlatformStatDataSource {
    fun getStats(): Flow<List<PlatformStatDataModel>>
}