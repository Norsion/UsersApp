package com.nanotech.app_test.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.MutableLiveData


object NetworkMonitor {
    var isConnected: Boolean = false
    val isConnectedLive = MutableLiveData(false)
    val networkTypeLive = MutableLiveData(NetworkType.NONE)

    private lateinit var cm: ConnectivityManager

    fun registerNetworkMonitor(context: Context) {
        cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            cm.activeNetworkInfo?.type.let {
                obtainNetworkType(it).also {networkType ->
                    networkTypeLive.postValue(networkType)
                }
            }
        } else {
            obtainNetworkType(cm.activeNetwork?.let { cm.getNetworkCapabilities(it) })
                .also {
                    networkTypeLive.postValue(it) }
        }

        cm.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    networkTypeLive.postValue(obtainNetworkType(networkCapabilities))
                }

                override fun onLost(network: Network) {
                    isConnected = false
                    isConnectedLive.postValue(false)
                    networkTypeLive.postValue(NetworkType.NONE)
                }

                override fun onAvailable(network: Network) {
                    isConnected = true
                    isConnectedLive.postValue(true)
                    setNetworkIsConnected(true)
                }
            }
        )
    }

    private fun obtainNetworkType(networkType: Int?): NetworkType = when (networkType) {
        null -> NetworkType.NONE
        ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
        ConnectivityManager.TYPE_MOBILE -> NetworkType.CELLULAR
        else -> NetworkType.UNKNOWN
    }

    private fun obtainNetworkType(networkCapabilities: NetworkCapabilities?): NetworkType = when {
        networkCapabilities == null -> NetworkType.NONE
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
        else -> NetworkType.UNKNOWN
    }

    fun setNetworkIsConnected(isConnected: Boolean = true) {
        this.isConnected = isConnected
    }
}

enum class NetworkType {
    NONE, UNKNOWN, WIFI, CELLULAR
}