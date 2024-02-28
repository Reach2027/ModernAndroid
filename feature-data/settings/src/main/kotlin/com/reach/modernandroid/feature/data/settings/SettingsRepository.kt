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

package com.reach.modernandroid.feature.data.settings

import com.reach.modernandroid.core.data.datastore.SettingsLocalSource
import com.reach.modernandroid.core.data.datastore.model.DarkThemeConfig
import com.reach.modernandroid.core.data.datastore.model.UserSetting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface SettingsRepository {

    val userSetting: StateFlow<UserSetting>

    fun setDynamicTheme(dynamicTheme: Boolean)

    fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
}

internal class DefaultSettingsRepository(
    private val settingsLocalSource: SettingsLocalSource,
    private val coroutineScope: CoroutineScope,
) : SettingsRepository {

    override val userSetting: StateFlow<UserSetting> = settingsLocalSource.settings
    override fun setDynamicTheme(dynamicTheme: Boolean) {
        coroutineScope.launch {
            settingsLocalSource.setDynamicTheme(dynamicTheme)
        }
    }

    override fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        coroutineScope.launch {
            settingsLocalSource.setDarkThemeConfig(darkThemeConfig)
        }
    }
}
