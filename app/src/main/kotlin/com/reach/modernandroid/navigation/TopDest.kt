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

package com.reach.modernandroid.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.core.ui.resource.AppIcons

enum class TopDest(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    More(
        route = AppRoute.MORE,
        selectedIcon = AppIcons.MoreSelected,
        unselectedIcon = AppIcons.MoreUnselected,
    ),
    ME(
        route = AppRoute.ME,
        selectedIcon = AppIcons.MeSelected,
        unselectedIcon = AppIcons.MeUnselected,
    ),
}

fun NavHostController.navToTopDest(topDest: TopDest) {
    val topNavOptions = navOptions {
        popUpTo(this@navToTopDest.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    when (topDest) {
        TopDest.ME -> this.navigate(AppRoute.ME, topNavOptions)
        TopDest.More -> this.navigate(AppRoute.MORE, topNavOptions)
    }
}

fun NavDestination?.isTopDestInHierarchy(topDest: TopDest) =
    this?.hierarchy?.any {
        it.route?.contains(topDest.route, true) ?: false
    } ?: false

fun NavDestination?.isTopDest() =
    TopDest.entries.any { topDest ->
        topDest.route.equals(this?.route, true)
    }
