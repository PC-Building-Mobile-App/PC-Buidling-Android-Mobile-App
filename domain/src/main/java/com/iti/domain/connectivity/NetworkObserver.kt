package com.iti.domain.connectivity

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    val connectionStatus: Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}
