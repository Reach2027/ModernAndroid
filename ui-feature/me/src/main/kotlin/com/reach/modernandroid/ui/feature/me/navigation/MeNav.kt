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

package com.reach.modernandroid.ui.feature.me.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.reach.modernandroid.ui.feature.me.MeRoute

const val ROUTE_ME = "route_me"

fun NavController.navToMe(navOptions: NavOptions) = navigate(ROUTE_ME, navOptions)

fun NavGraphBuilder.meRoute() {
    composable(
        route = ROUTE_ME,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        MeRoute()
    }
}
