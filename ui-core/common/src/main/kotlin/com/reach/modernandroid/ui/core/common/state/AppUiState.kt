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

package com.reach.modernandroid.ui.core.common.state

import android.content.pm.ActivityInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AppUiState {

    val statusDarkMode: StateFlow<StatusDarkMode>

    val fullScreen: StateFlow<Boolean>

    val requestedOrientation: StateFlow<Int>

    fun getWindowSizeClass(): WindowSizeClass

    fun setWindowSizeClass(windowSizeClass: WindowSizeClass)

    fun getNavController(): NavHostController

    fun setNavController(navController: NavHostController)

    fun setStatusDarkMode(statusDarkMode: StatusDarkMode)

    fun setFullScreen(fullScreen: Boolean)

    fun setRequestedOrientation(requestedOrientation: Int)

    fun resetRequestedOrientation()
}

internal class DefaultAppUiState : AppUiState {

    private var windowSizeClass: WindowSizeClass? = null

    private var navController: NavHostController? = null

    private val _statusDarkMode = MutableStateFlow(StatusDarkMode.FollowTheme)
    override val statusDarkMode = _statusDarkMode.asStateFlow()

    private val _fullScreen = MutableStateFlow(false)
    override val fullScreen = _fullScreen.asStateFlow()

    private val _requestedOrientation =
        MutableStateFlow(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    override val requestedOrientation = _requestedOrientation.asStateFlow()

    override fun getWindowSizeClass(): WindowSizeClass = requireNotNull(windowSizeClass)

    override fun setWindowSizeClass(windowSizeClass: WindowSizeClass) {
        if (this.windowSizeClass != windowSizeClass) {
            this.windowSizeClass = windowSizeClass
        }
    }

    override fun getNavController(): NavHostController = requireNotNull(navController)

    override fun setNavController(navController: NavHostController) {
        if (this.navController != navController) {
            this.navController = navController
        }
    }

    override fun setStatusDarkMode(statusDarkMode: StatusDarkMode) {
        _statusDarkMode.value = statusDarkMode
    }

    override fun setFullScreen(fullScreen: Boolean) {
        _fullScreen.value = fullScreen
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        _requestedOrientation.value = requestedOrientation
    }

    override fun resetRequestedOrientation() {
        _requestedOrientation.value = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
