package com.iti.domain.stats.repository

import com.iti.domain.stats.model.PlatformStat
import kotlinx.coroutines.flow.Flow

interface PlatformStatRepository {
    fun getPlatformStats(): Flow<List<PlatformStat>>
}