/*
 * Copyright 2023 Reach Project
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

package com.reach.modernandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reach.modernandroid.core.data.datastore.model.DarkThemeConfig
import com.reach.modernandroid.core.ui.design.theme.AppTheme
import com.reach.modernandroid.feature.data.settings.SettingsRepository
import com.reach.modernandroid.ui.App
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    val settingsRepo: SettingsRepository by inject()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            val settings by settingsRepo.userSetting.collectAsStateWithLifecycle()
            AppTheme(
                dynamicTheme = settings.dynamicColor,
                darkTheme = when (settings.darkThemeConfig) {
                    DarkThemeConfig.Light -> false
                    DarkThemeConfig.Dark -> true
                    DarkThemeConfig.FollowSystem -> isSystemInDarkTheme()
                },
            ) {
                App(windowSizeClass = calculateWindowSizeClass(activity = this))
            }
        }
    }
}
