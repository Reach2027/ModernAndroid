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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.reach.modernandroid.ui.core.resource.AppIcons
import com.reach.modernandroid.ui.feature.me.navigation.navToMe
import com.reach.modernandroid.ui.feature.more.navigation.navToMore

enum class TopDest(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    More(
        selectedIcon = AppIcons.MoreSelected,
        unselectedIcon = AppIcons.MoreUnselected,
    ),
    ME(
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
        TopDest.ME -> this.navToMe(topNavOptions)
        TopDest.More -> this.navToMore(topNavOptions)
    }
}
