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

package com.reach.modernandroid.core.data.datastore

import androidx.datastore.core.DataStore
import com.reach.modernandroid.core.data.datastore.model.DarkThemeConfig
import com.reach.modernandroid.core.data.datastore.model.UserSetting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

interface SettingsLocalSource {

    val settings: StateFlow<UserSetting>

    suspend fun setDynamicTheme(dynamicTheme: Boolean)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
}

internal class DefaultSettingsLocalSource(
    private val dataStore: DataStore<SettingsPb>,
    coroutineScope: CoroutineScope,
) : SettingsLocalSource {

    override val settings: StateFlow<UserSetting> = dataStore.data.map {
        UserSetting(
            dynamicColor = it.dynamicTheme,
            darkThemeConfig = when (it.darkThemeConfig) {
                1 -> DarkThemeConfig.Light
                2 -> DarkThemeConfig.Dark
                else -> DarkThemeConfig.FollowSystem
            },
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = UserSetting(),
    )

    override suspend fun setDynamicTheme(dynamicTheme: Boolean) {
        dataStore.updateData {
            it.copy { this.dynamicTheme = dynamicTheme }
        }
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataStore.updateData {
            it.copy { this.darkThemeConfig = darkThemeConfig.ordinal }
        }
    }
}
