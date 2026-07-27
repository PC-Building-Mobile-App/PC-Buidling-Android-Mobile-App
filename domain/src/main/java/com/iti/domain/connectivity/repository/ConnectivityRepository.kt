package com.iti.domain.connectivity.repository

import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {
    val connectionStatus: Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}
