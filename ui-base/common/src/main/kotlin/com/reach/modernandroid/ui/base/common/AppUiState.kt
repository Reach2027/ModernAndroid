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

package com.reach.modernandroid.ui.base.common

import androidx.navigation.NavHostController
import com.reach.modernandroid.ui.base.common.navigation.AppRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface AppUiState {

    var navController: NavHostController

    val isFullScreen: StateFlow<Boolean>

    fun setup(navController: NavHostController)
}

internal class DefaultAppUiState(
    private val coroutineScope: CoroutineScope,
) : AppUiState {

    override lateinit var navController: NavHostController

    private val _isFullScreen = MutableStateFlow(false)
    override val isFullScreen = _isFullScreen.asStateFlow()

    private var navJob: Job? = null

    override fun setup(navController: NavHostController) {
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
}
