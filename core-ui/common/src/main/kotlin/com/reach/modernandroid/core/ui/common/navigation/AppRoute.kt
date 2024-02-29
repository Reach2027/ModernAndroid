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

package com.reach.modernandroid.core.ui.common.navigation

object AppRoute {
    // Top destination
    const val ME = "route_me"
    const val MORE = "route_more"

    // Feature
    const val SETTINGS = "route_settings"

    const val ALBUM = "route_album"

    const val CAMERAX = "route_camerax"

    const val LOTTIE = "route_lottie"

    const val SKELETON_LOADER = "route_skeleton_loader"

    const val BING_WALLPAPER = "route_bing_wallpaper"

    internal val fullScreenRoute = arrayOf(ME, BING_WALLPAPER, ALBUM)
}
