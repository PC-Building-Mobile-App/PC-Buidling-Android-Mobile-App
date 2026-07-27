package com.iti.domain.connectivity.usecase

import com.iti.domain.connectivity.repository.ConnectivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveNetworkStatusUseCase @Inject constructor(
    private val connectivityRepository: ConnectivityRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return connectivityRepository.connectionStatus.map { status ->
            status == ConnectivityRepository.Status.Available
        }
    }
}