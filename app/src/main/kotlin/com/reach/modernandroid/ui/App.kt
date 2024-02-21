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

package com.reach.modernandroid.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.reach.modernandroid.navigation.AppNavHost
import com.reach.modernandroid.navigation.TopDest
import com.reach.modernandroid.navigation.isTopDest
import com.reach.modernandroid.navigation.isTopDestInHierarchy
import com.reach.modernandroid.navigation.navToTopDest
import com.reach.modernandroid.ui.base.common.AppUiState
import com.reach.modernandroid.ui.base.common.animation.AppAniSpec
import com.reach.modernandroid.ui.base.common.animation.widgetEnter
import com.reach.modernandroid.ui.base.common.animation.widgetExit
import org.koin.compose.koinInject

@Composable
internal fun App(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
) {
    val appUiState: AppUiState = koinInject()
    appUiState.setup(
        windowSizeClass = windowSizeClass,
        navController = navController,
    )

    AppScreen(
        appUiState = appUiState,
    )
}

@Composable
private fun AppScreen(
    appUiState: AppUiState,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val currentDest = appUiState.getNavController()
        .currentBackStackEntryAsState()
        .value
        ?.destination

    val fullScreen by appUiState.isFullScreen.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = currentDest.isTopDest(),
                enter = slideInVertically(
                    animationSpec = widgetEnter(
                        dampingRatio = AppAniSpec.DAMPING_RATIO,
                        visibilityThreshold = IntOffset.VisibilityThreshold,
                    ),
                    initialOffsetY = { it },
                ),
                exit = slideOutVertically(
                    animationSpec = widgetExit(
                        visibilityThreshold = IntOffset.VisibilityThreshold,
                    ),
                    targetOffsetY = { it },
                ),
            ) {
                AppNavBar(
                    destList = TopDest.entries,
                    onNavToTopDest = { appUiState.getNavController().navToTopDest(it) },
                    currentDest = currentDest,
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = if (fullScreen) {
            WindowInsets(0, 0, 0, 0)
        } else {
            WindowInsets.statusBars
        },
    ) { padding ->
        AppNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding),
            navController = appUiState.getNavController(),
        )
    }
}

@Composable
private fun AppNavBar(
    destList: List<TopDest>,
    onNavToTopDest: (TopDest) -> Unit,
    currentDest: NavDestination?,
) {
    NavigationBar {
        destList.forEach { topDest ->
            val selected = currentDest.isTopDestInHierarchy(topDest)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavToTopDest(topDest) },
                icon = {
                    Icon(
                        imageVector = if (selected) topDest.selectedIcon else topDest.unselectedIcon,
                        contentDescription = "",
                    )
                },
            )
        }
    }
}
