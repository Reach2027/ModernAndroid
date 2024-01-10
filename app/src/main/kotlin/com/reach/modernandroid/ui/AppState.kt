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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.reach.modernandroid.navigation.TopDest
import com.reach.modernandroid.ui.feature.me.navigation.ME_ROUTE
import com.reach.modernandroid.ui.feature.me.navigation.navToMe
import com.reach.modernandroid.ui.feature.more.navigation.MORE_ROUTE
import com.reach.modernandroid.ui.feature.more.navigation.navToMore

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(navController) {
        AppState(navController)
    }
}

@Stable
class AppState(
    val navController: NavHostController,
) {
    val currentDest: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopDest: TopDest?
        @Composable get() = when (currentDest?.route) {
            ME_ROUTE -> TopDest.ME
            MORE_ROUTE -> TopDest.More
            else -> null
        }

    val topDestList: List<TopDest> = TopDest.entries

    fun navToTopDest(topDest: TopDest) {
        val topNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topDest) {
            TopDest.ME -> navController.navToMe(topNavOptions)
            TopDest.More -> navController.navToMore(topNavOptions)
        }
    }
}
