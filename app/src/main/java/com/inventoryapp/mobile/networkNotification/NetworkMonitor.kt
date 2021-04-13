package com.inventoryapp.mobile.networkNotification

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import javax.inject.Inject

class NetworkMonitor @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : LiveData<NetworkStatus>() {
    private val networkRequest = NetworkRequest.Builder().apply {
        addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED)
        addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
    }

    private val networkCallBack by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                this@NetworkMonitor.postValue(NetworkStatus.Offline)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                this@NetworkMonitor.postValue(NetworkStatus.Offline)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                this@NetworkMonitor.postValue(NetworkStatus.Online)
            }
        }
    }

    override fun onActive() {
        super.onActive()
        connectivityManager.registerNetworkCallback(networkRequest.build(), networkCallBack)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallBack)
    }

    fun isOnline() = connectivityManager.activeNetworkInfo?.isConnected ?: false
}

sealed class NetworkStatus {
    object Online : NetworkStatus()
    object Offline : NetworkStatus()
}