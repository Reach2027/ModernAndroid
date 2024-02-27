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

package com.reach.modernandroid.data.feature.setting

import com.reach.modernandroid.data.base.datastore.SettingsLocalSource
import com.reach.modernandroid.data.base.datastore.model.DarkThemeConfig
import com.reach.modernandroid.data.base.datastore.model.UserSetting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val userSetting: StateFlow<UserSetting>

    suspend fun setDynamicTheme(dynamicTheme: Boolean)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
}

internal class DefaultSettingsRepository(
    private val settingsLocalSource: SettingsLocalSource,
    private val coroutineScope: CoroutineScope,
) : SettingsRepository {

    override val userSetting: StateFlow<UserSetting> = settingsLocalSource.settings
    override suspend fun setDynamicTheme(dynamicTheme: Boolean) {
        settingsLocalSource.setDynamicTheme(dynamicTheme)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        settingsLocalSource.setDarkThemeConfig(darkThemeConfig)
    }
}
