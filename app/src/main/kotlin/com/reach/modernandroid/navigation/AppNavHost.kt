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
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.feature.album.albumRoute
import com.reach.modernandroid.feature.bingwallpaper.bingWallpaperRoute
import com.reach.modernandroid.feature.camerax.cameraxRoute
import com.reach.modernandroid.feature.me.meRoute
import com.reach.modernandroid.feature.more.moreRoute
import com.reach.modernandroid.feature.settings.settingsRoute
import com.reach.modernandroid.feature.uilearn.lottieRoute
import com.reach.modernandroid.feature.uilearn.skeletonLoaderRoute

@Composable
internal fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoute.ME,
    ) {
        meRoute()

        moreRoute()

        settingsRoute()

        lottieRoute()

        skeletonLoaderRoute()

        bingWallpaperRoute()

        albumRoute()

        cameraxRoute()
    }
}
