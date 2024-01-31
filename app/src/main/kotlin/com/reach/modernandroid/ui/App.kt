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
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.reach.modernandroid.navigation.AppNavHost
import com.reach.modernandroid.navigation.TopDest
import com.reach.modernandroid.navigation.isTopDest
import com.reach.modernandroid.navigation.isTopDestInHierarchy
import com.reach.modernandroid.navigation.navToTopDest
import com.reach.modernandroid.ui.base.common.LocalAppUiState

@Composable
internal fun App(
    navController: NavHostController = rememberNavController(),
) {
    CompositionLocalProvider(
        LocalAppUiState provides DefaultAppUiState(navController),
    ) {
        AppScreen(navController = navController)
    }
}

@Composable
private fun AppScreen(
    navController: NavHostController = LocalAppUiState.current.navController,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val currentDest = navController.currentBackStackEntryAsState().value?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = currentDest.isTopDest(),
                enter = slideInVertically(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                        visibilityThreshold = IntOffset.VisibilityThreshold,
                    ),
                    initialOffsetY = { it },
                ),
                exit = slideOutVertically(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                        visibilityThreshold = IntOffset.VisibilityThreshold,
                    ),
                    targetOffsetY = { it },
                ),
            ) {
                AppNavBar(
                    destList = TopDest.entries,
                    onNavToTopDest = { navController.navToTopDest(it) },
                    currentDest = currentDest,
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        AppNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            navController = navController,
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
