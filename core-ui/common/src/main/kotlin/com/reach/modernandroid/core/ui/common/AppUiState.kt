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

package com.reach.modernandroid.core.ui.common

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavHostController
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface AppUiState {

    val isFullScreen: StateFlow<Boolean>

    fun setup(
        windowSizeClass: WindowSizeClass,
        navController: NavHostController,
    )

    fun getWindowSizeClass(): WindowSizeClass

    fun getNavController(): NavHostController

    fun setFullScreen(fullScreen: Boolean)
}

internal class DefaultAppUiState(
    private val coroutineScope: CoroutineScope,
) : AppUiState {

    private var windowSizeClass: WindowSizeClass? = null

    private var navController: NavHostController? = null

    private val _isFullScreen = MutableStateFlow(false)
    override val isFullScreen = _isFullScreen.asStateFlow()

    private var navJob: Job? = null

    override fun setup(
        windowSizeClass: WindowSizeClass,
        navController: NavHostController,
    ) {
        this.windowSizeClass = windowSizeClass
        this.navController = navController

        navJob?.apply {
            if (isActive) {
                cancel()
            }
        }

        navJob = coroutineScope.launch {
            navController.currentBackStackEntryFlow.collect {
                val needFullScreen = AppRoute.fullScreenRoute.contains(it.destination.route)
                _isFullScreen.emit(needFullScreen)
            }
        }
    }

    override fun getWindowSizeClass(): WindowSizeClass {
        return requireNotNull(windowSizeClass)
    }

    override fun getNavController(): NavHostController {
        return requireNotNull(navController)
    }

    override fun setFullScreen(fullScreen: Boolean) {
        _isFullScreen.value = fullScreen
    }
}