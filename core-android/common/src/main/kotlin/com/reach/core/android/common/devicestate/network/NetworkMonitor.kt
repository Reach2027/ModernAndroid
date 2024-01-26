/*
 * Copyright 2024 Reach Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reach.core.android.common.devicestate.network

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class NetworkMonitor(
    application: Application,
    private val appIoScope: CoroutineScope,
) {

    private val _isOnline = MutableStateFlow(false)
    val isOnline = _isOnline.asStateFlow()

    private val _networkType = MutableStateFlow(NetworkType.None)
    val networkType = _networkType.asStateFlow()

    private val connectivityManager: ConnectivityManager? =
        application.getSystemService<ConnectivityManager>()

    private val callback = object : ConnectivityManager.NetworkCallback() {
        private val networks = mutableSetOf<Network>()

        override fun onAvailable(network: Network) {
            networks += network
            _isOnline.tryEmit(true)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities,
        ) {
            _networkType.tryEmit(networkCapabilities.getNetworkType())
        }

        override fun onLost(network: Network) {
            networks -= network
            _isOnline.tryEmit(networks.isNotEmpty())
        }
    }

    init {
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        if (connectivityManager == null) {
            return
        }

        appIoScope.launch {
            connectivityManager.currentNetwork()

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(request, callback)
        }
    }

    @Suppress("DEPRECATION")
    private fun ConnectivityManager.currentNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = activeNetwork?.let(::getNetworkCapabilities)
            capabilities?.apply {
                _isOnline.tryEmit(hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                _networkType.tryEmit(getNetworkType())
            } ?: apply {
                _isOnline.tryEmit(false)
                _networkType.tryEmit(NetworkType.None)
            }
        } else {
            activeNetworkInfo?.apply {
                _isOnline.tryEmit(isConnected)
                _networkType.tryEmit(getNetworkType())
            } ?: apply {
                _isOnline.tryEmit(false)
                _networkType.tryEmit(NetworkType.None)
            }
        }
    }

    private fun NetworkCapabilities.getNetworkType() = when {
        _isOnline.value.not() -> NetworkType.None
        this.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.Eth
        this.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WiFi
        this.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.Mobile
        else -> NetworkType.Unknown
    }

    @Suppress("DEPRECATION")
    private fun NetworkInfo.getNetworkType() = when {
        _isOnline.value.not() -> NetworkType.None
        type == ConnectivityManager.TYPE_ETHERNET -> NetworkType.Eth
        type == ConnectivityManager.TYPE_WIFI -> NetworkType.WiFi
        type == ConnectivityManager.TYPE_MOBILE -> NetworkType.Mobile
        else -> NetworkType.Unknown
    }
}
