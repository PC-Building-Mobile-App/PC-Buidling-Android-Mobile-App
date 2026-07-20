package com.iti.data.components.datasource

import com.iti.data.components.model.ComponentDataModel
import com.iti.data.components.remote.ComponentApiService
import com.iti.data.util.safeCall
import com.iti.domain.exceptions.ServerException
import javax.inject.Inject

class ComponentRemoteDataSourceImpl @Inject constructor(
    private val apiService: ComponentApiService
) : ComponentRemoteDataSource {

    override suspend fun getRandomComponents(category: String, limit: Int): Result<List<ComponentDataModel>> = safeCall {
        val response = apiService.getRandomDeals(category, limit)
        if (response.status) {
            response.data
        } else {
            throw ServerException.Generic(
                message = response.message,
                code = 400
            )
        }
    }
}