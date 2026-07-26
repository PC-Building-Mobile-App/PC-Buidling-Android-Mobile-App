package com.iti.data.stats.datasource

import com.iti.data.stats.model.PlatformStatDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PlatformStatLocalDataSourceImpl @Inject constructor() : PlatformStatDataSource {

    override fun getStats(): Flow<List<PlatformStatDataModel>> {
        val staticStats = listOf(
            PlatformStatDataModel(
                id = "BUILDS",
                label = "BUILDS",
                count = 12000,
                isApproximated = true
            ),
            PlatformStatDataModel(
                id = "PARTS",
                label = "PARTS",
                count = 1446,
                isApproximated = false
            ),
            PlatformStatDataModel(
                id = "STORES",
                label = "STORES",
                count = 2,
                isApproximated = false
            )
        )
        return flowOf(staticStats)
    }
}