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

package com.reach.core.android.common.di

import com.reach.core.android.common.devicestate.DefaultDeviceState
import com.reach.core.android.common.devicestate.DeviceState
import com.reach.core.android.common.devicestate.network.NetworkMonitor
import com.reach.core.jvm.common.di.QualifierCoroutineScope
import com.reach.core.jvm.common.di.coroutineScopeModule
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val deviceStateModule = module {
    includes(coroutineScopeModule)

    single { NetworkMonitor(androidApplication(), get(qualifier(QualifierCoroutineScope.AppIo))) }

    singleOf(::DefaultDeviceState) { bind<DeviceState>() }
}
