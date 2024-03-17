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

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.reach.modernandroid.core.data.datastore.model.DarkThemeConfig
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.core.ui.common.state.StatusDarkMode
import com.reach.modernandroid.core.ui.design.theme.AppTheme
import com.reach.modernandroid.ui.App
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    private val appUiState: AppUiState by inject()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            appUiState.requestedOrientation.collectLatest {
                if (requestedOrientation != it) {
                    requestedOrientation = it
                }
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setupFullScreen()

        val splashScreen = installSplashScreen()

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState())
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState = it }
        }

        splashScreen.setKeepOnScreenCondition { uiState.isLoading }

        var statusDarkMode: StatusDarkMode by mutableStateOf(StatusDarkMode.FollowTheme)
        lifecycleScope.launch {
            appUiState.statusDarkMode.collectLatest { statusDarkMode = it }
        }

        setContent {
            val darkTheme = when (uiState.settings.darkThemeConfig) {
                DarkThemeConfig.Light -> false
                DarkThemeConfig.Dark -> true
                DarkThemeConfig.FollowSystem -> isSystemInDarkTheme()
            }

            LaunchedEffect(darkTheme, statusDarkMode) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                    ) {
                        when (statusDarkMode) {
                            StatusDarkMode.FollowTheme -> darkTheme
                            StatusDarkMode.Light -> false
                            StatusDarkMode.Dark -> true
                        }
                    },
                    navigationBarStyle = SystemBarStyle.auto(
                        LightScrim,
                        DarkScrim,
                    ) { darkTheme },
                )
            }

            AppTheme(
                dynamicTheme = uiState.settings.dynamicColor,
                darkTheme = darkTheme,
            ) {
                App(windowSizeClass = calculateWindowSizeClass(activity = this))
            }
        }
    }

    private fun setupFullScreen() {
        /*val insetsController = WindowCompat.getInsetsController(window, window.decorView.rootView)*/
        lifecycleScope.launch {
            appUiState.fullScreen.collectLatest {
                // The animation is too slow
                /*if (it) {
                    insetsController.hide(WindowInsetsCompat.Type.systemBars())
                } else {
                    insetsController.show(WindowInsetsCompat.Type.systemBars())
                }*/
                if (it) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
            }
        }
    }
}

private val LightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
