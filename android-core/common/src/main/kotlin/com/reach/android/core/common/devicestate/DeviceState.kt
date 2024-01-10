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

package com.reach.android.core.common.devicestate

import com.reach.android.core.common.devicestate.network.NetworkMonitor
import com.reach.android.core.common.devicestate.network.NetworkType
import kotlinx.coroutines.flow.StateFlow

interface DeviceState {

    val isOnline: StateFlow<Boolean>

    val networkType: StateFlow<NetworkType>
}

internal class DefaultDeviceState(
    networkMonitor: NetworkMonitor,
) : DeviceState {

    override val isOnline: StateFlow<Boolean> = networkMonitor.isOnline

    override val networkType: StateFlow<NetworkType> = networkMonitor.networkType
}
