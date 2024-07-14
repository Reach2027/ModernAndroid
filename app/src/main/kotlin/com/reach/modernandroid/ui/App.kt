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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.navigation.AppNavHost
import com.reach.modernandroid.navigation.TopDest
import com.reach.modernandroid.navigation.isTopDest
import com.reach.modernandroid.navigation.isTopDestInHierarchy
import com.reach.modernandroid.navigation.navToTopDest
import org.koin.compose.koinInject

@Composable
internal fun App(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
) {
    val appUiState: AppUiState = koinInject()
    appUiState.setWindowSizeClass(windowSizeClass)
    appUiState.setNavController(navController)

    AppScreen(
        appUiState = appUiState,
    )
}

@Composable
private fun AppScreen(
    appUiState: AppUiState,
) {
    val currentDest = appUiState.getNavController()
        .currentBackStackEntryAsState()
        .value
        ?.destination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopDest.entries.forEach { topDest ->
                val selected = currentDest.isTopDestInHierarchy(topDest)
                item(
                    selected = selected,
                    onClick = { appUiState.getNavController().navToTopDest(topDest) },
                    icon = {
                        Icon(
                            imageVector = if (selected) topDest.selectedIcon else topDest.unselectedIcon,
                            contentDescription = "",
                        )
                    },
                )
            }
        },
        layoutType = if (currentDest.isTopDest()) {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
        } else {
            NavigationSuiteType.None
        },
    ) {
        AppNavHost(
            modifier = Modifier.fillMaxSize(),
            navController = appUiState.getNavController(),
        )
    }
}
