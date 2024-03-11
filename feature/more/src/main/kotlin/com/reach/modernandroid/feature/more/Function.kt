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

package com.reach.modernandroid.feature.more

import androidx.navigation.NavController
import com.reach.modernandroid.core.ui.common.navigation.AppRoute

internal enum class Function(
    val text: String,
    val navTo: (NavController) -> Unit,
    val isTitle: Boolean,
) {
    ComposeLearn(
        text = "Compose Learn",
        navTo = {},
        isTitle = true,
    ),
    Lottie(
        text = "Lottie Example",
        navTo = { it.navigate(AppRoute.LOTTIE) },
        isTitle = false,
    ),
    SkeletonLoader(
        text = "Skeleton Loader",
        navTo = { it.navigate(AppRoute.SKELETON_LOADER) },
        isTitle = false,
    ),
    Feature(
        text = "Feature",
        navTo = {},
        isTitle = true,
    ),
    BingWallpaper(
        text = "Bing Wallpaper",
        navTo = { it.navigate(AppRoute.BING_WALLPAPER) },
        isTitle = false,
    ),
    LocalAlbum(
        text = "Local Album",
        navTo = { it.navigate(AppRoute.ALBUM) },
        isTitle = false,
    ),
    Camerax(
        text = "Camerax",
        navTo = { it.navigate(AppRoute.CAMERAX) },
        isTitle = false,
    ),
}
