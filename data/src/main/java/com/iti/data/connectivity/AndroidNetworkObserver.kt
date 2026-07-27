package com.iti.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.iti.domain.connectivity.NetworkObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class AndroidNetworkObserver(
    context: Context
) : NetworkObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val connectionStatus: Flow<NetworkObserver.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(NetworkObserver.Status.Available)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                trySend(NetworkObserver.Status.Losing)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(NetworkObserver.Status.Lost)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(NetworkObserver.Status.Unavailable)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}
