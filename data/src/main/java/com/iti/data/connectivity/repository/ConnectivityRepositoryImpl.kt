package com.iti.data.connectivity.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.iti.domain.connectivity.repository.ConnectivityRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ConnectivityRepositoryImpl @Inject constructor(
    context: Context
) : ConnectivityRepository {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val connectionStatus: Flow<ConnectivityRepository.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(ConnectivityRepository.Status.Available)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                trySend(ConnectivityRepository.Status.Losing)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(ConnectivityRepository.Status.Lost)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(ConnectivityRepository.Status.Unavailable)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}
