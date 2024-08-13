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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.reach.modernandroid.feature.album.navigation.albumGraph
import com.reach.modernandroid.feature.bingwallpaper.bingWallpaperRoute
import com.reach.modernandroid.feature.camerax.cameraxRoute
import com.reach.modernandroid.feature.me.meRoute
import com.reach.modernandroid.feature.more.moreRoute
import com.reach.modernandroid.feature.settings.settingsRoute
import com.reach.modernandroid.feature.uilearn.lottieRoute
import com.reach.modernandroid.feature.uilearn.skeletonLoaderRoute
import com.reach.modernandroid.ui.core.common.navigation.AppRoute
import com.reach.modernandroid.ui.core.design.animation.enterScreenTransition
import com.reach.modernandroid.ui.core.design.animation.exitScreenTransition
import com.reach.modernandroid.ui.core.design.animation.popEnterScreenTransition
import com.reach.modernandroid.ui.core.design.animation.popExitScreenTransition

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoute.Me,
        enterTransition = { enterScreenTransition() },
        exitTransition = { exitScreenTransition() },
        popEnterTransition = { popEnterScreenTransition() },
        popExitTransition = { popExitScreenTransition() },
    ) {
        meRoute()

        moreRoute()

        settingsRoute()

        lottieRoute()

        skeletonLoaderRoute()

        bingWallpaperRoute()

        albumGraph()

        cameraxRoute()
    }
}
