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

package com.reach.modernandroid.ui.feature.lottie.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.reach.modernandroid.ui.base.common.navigation.screenComposable
import com.reach.modernandroid.ui.feature.lottie.LottieRoute

const val ROUTE_LOTTIE = "route_lottie"

fun NavController.navToLottie(navOptions: NavOptions? = null) =
    navigate(ROUTE_LOTTIE, navOptions)

fun NavGraphBuilder.lottieRoute() {
    screenComposable(ROUTE_LOTTIE) {
        LottieRoute()
    }
}
