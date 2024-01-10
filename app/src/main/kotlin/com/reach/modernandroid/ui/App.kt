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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.reach.modernandroid.navigation.AppNavHost
import com.reach.modernandroid.navigation.TopDest

@Composable
fun App(
    appState: AppState = rememberAppState(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = {
            AppNavBar(
                destList = appState.topDestList,
                onNavToTopDest = appState::navToTopDest,
                currentDest = appState.currentDest,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            AppNavHost(appState.navController)
        }
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

private fun NavDestination?.isTopDestInHierarchy(topDest: TopDest) =
    this?.hierarchy?.any {
        it.route?.contains(topDest.name, true) ?: false
    } ?: false
