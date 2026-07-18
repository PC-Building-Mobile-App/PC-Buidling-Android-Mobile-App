package com.iti.domain.stats.usecase

import com.iti.domain.stats.model.PlatformStat
import com.iti.domain.stats.repository.PlatformStatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlatformStatsUseCase @Inject constructor(
    private val repository: PlatformStatRepository
) {
    operator fun invoke(): Flow<List<PlatformStat>> {
        return repository.getPlatformStats()
    }
}