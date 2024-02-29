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

package com.reach.modernandroid.feature.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.reach.base.ui.common.toDp
import com.reach.base.ui.common.widget.SkeletonAsyncImage
import com.reach.modernandroid.core.ui.common.AppPreview
import com.reach.modernandroid.core.ui.common.AppUiState
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.core.ui.common.navigation.screenComposable
import com.reach.modernandroid.core.ui.common.widget.AppTopBarWithBack
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

fun NavGraphBuilder.albumRoute() {
    screenComposable(
        route = AppRoute.ALBUM,
    ) {
        AlbumRoute()
    }
}

@Composable
private fun AlbumRoute(
    appUiState: AppUiState = koinInject(),
    viewModel: AlbumViewModel = koinNavViewModel(),
) {
    AlbumScreen(
        onBackClick = { appUiState.getNavController().navigateUp() },
        windowSizeClass = appUiState.getWindowSizeClass(),
        localImages = viewModel.localImages,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumScreen(
    onBackClick: () -> Unit,
    windowSizeClass: WindowSizeClass,
    localImages: Flow<PagingData<LocalImageModel>>,
) {
    val fixedCount = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 3
        WindowWidthSizeClass.Medium -> 5
        else -> 6
    }

    val items: LazyPagingItems<LocalImageModel> = localImages.collectAsLazyPagingItems()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val systemBarH = WindowInsets.systemBars.getTop(LocalDensity.current).toDp()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(fixedCount),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                Spacer(modifier = Modifier.height(systemBarH + 64.dp))
            }
            when (items.loadState.refresh) {
                is LoadState.Loading -> {}

                is LoadState.Error -> {}

                is LoadState.NotLoading -> {
                    items(
                        count = items.itemCount,
                        key = items.itemKey { it.id },
                    ) { index ->
                        LocalImageItem(items[index])
                    }
                }
            }
        }

        AppTopBarWithBack(
            title = { Text(text = stringResource(id = R.string.photos)) },
            onBackClick = onBackClick,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            ),
            scrollBehavior = scrollBehavior,
        )
    }
}

@Composable
private fun LocalImageItem(localImageModel: LocalImageModel?) {
    if (localImageModel == null) return

    Box {
        SkeletonAsyncImage(
            model = localImageModel.uri,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            placeHolderModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview
@Composable
private fun AlbumScreenPreview() {
    AppPreview {
    }
}
