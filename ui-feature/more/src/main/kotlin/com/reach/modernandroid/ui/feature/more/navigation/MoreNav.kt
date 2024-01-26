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

package com.reach.modernandroid.ui.feature.more.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.reach.modernandroid.ui.feature.lottie.navigation.lottieRoute
import com.reach.modernandroid.ui.feature.more.MoreRoute
import com.reach.modernandroid.ui.feature.skeletonloader.navigation.skeletonLoaderRoute

private const val GRAPH_MORE = "graph_more"
const val ROUTE_MORE = "route_more"

fun NavController.navToMore(navOptions: NavOptions) = navigate(ROUTE_MORE, navOptions)

fun NavGraphBuilder.moreGraph() {
    navigation(startDestination = ROUTE_MORE, route = GRAPH_MORE) {
        moreRoute()

        lottieRoute()

        skeletonLoaderRoute()
    }
}

private fun NavGraphBuilder.moreRoute() {
    composable(
        route = ROUTE_MORE,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        MoreRoute()
    }
}
