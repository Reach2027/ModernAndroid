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

package com.reach.modernandroid.feature.album.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.core.ui.common.navigation.screenComposable
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.feature.album.AlbumRoute
import com.reach.modernandroid.feature.album.preview.PreviewRoute
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

private const val ALBUM_GRAPH = "ALBUM_GRAPH"

internal const val ROUTE_ALBUM_PREVIEW = "route_album_preview"

fun NavGraphBuilder.albumGraph() {
    navigation(
        startDestination = AppRoute.ALBUM,
        route = ALBUM_GRAPH,
    ) {
        screenComposable(route = AppRoute.ALBUM) {
            AlbumRoute()
        }

        screenComposable(route = ROUTE_ALBUM_PREVIEW) {
            val appUiState: AppUiState = koinInject()
            val albumEntry = remember(it) {
                appUiState.getNavController().getBackStackEntry(AppRoute.ALBUM)
            }
            PreviewRoute(
                viewModel = koinNavViewModel(viewModelStoreOwner = albumEntry),
                appUiState = appUiState,
            )
        }
    }
}
