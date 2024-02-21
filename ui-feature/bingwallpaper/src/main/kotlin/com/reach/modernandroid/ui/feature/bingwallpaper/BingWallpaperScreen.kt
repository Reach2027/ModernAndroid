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

package com.reach.modernandroid.ui.feature.bingwallpaper

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.reach.core.ui.common.devicepreview.DevicePreviews
import com.reach.core.ui.common.widget.SkeletonAsyncImage
import com.reach.core.ui.common.widget.SkeletonLoader
import com.reach.modernandroid.data.feature.bingwallpaper.model.BingWallpaperModel
import com.reach.modernandroid.ui.base.common.AppPreview
import com.reach.modernandroid.ui.base.common.AppUiState
import com.reach.modernandroid.ui.base.common.animation.widgetEnter
import com.reach.modernandroid.ui.base.common.animation.widgetExit
import com.reach.modernandroid.ui.base.common.navigation.AppRoute
import com.reach.modernandroid.ui.base.common.navigation.screenComposable
import com.reach.modernandroid.ui.base.common.widget.AppTopBarWithBack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

fun NavGraphBuilder.bingWallpaperRoute() {
    screenComposable(AppRoute.BING_WALLPAPER) {
        BingWallpaperRoute()
    }
}

@Composable
private fun BingWallpaperRoute(
    appUiState: AppUiState = koinInject(),
    viewModel: BingWallpaperViewModel = koinNavViewModel(),
) {
    BingWallpaperScreen(
        onBackClick = { appUiState.getNavController().navigateUp() },
        sourceFlow = viewModel.sourceFlow,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BingWallpaperScreen(
    onBackClick: () -> Unit,
    sourceFlow: Flow<PagingData<BingWallpaperModel>>,
) {
    val items: LazyPagingItems<BingWallpaperModel> = sourceFlow.collectAsLazyPagingItems()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val collapsedFraction by remember {
        derivedStateOf {
            scrollBehavior.state.collapsedFraction
        }
    }

    val systemBarH = WindowInsets.systemBars.getTop(LocalDensity.current)

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        AppTopBarWithBack(
            title = { Text(text = stringResource(id = R.string.bing_wallpaper)) },
            onBackClick = onBackClick,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            ),
            windowInsets = WindowInsets(0, (systemBarH * (1f - collapsedFraction)).toInt(), 0, 0),
            scrollBehavior = scrollBehavior,
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 390.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            when (items.loadState.refresh) {
                is LoadState.Loading -> {
                    item {
                        BingWallPaperItemLoading()
                    }
                }

                is LoadState.Error -> {}

                is LoadState.NotLoading -> {
                    items(
                        count = items.itemCount,
                        key = items.itemKey { it.imageUrl },
                    ) { index ->
                        BingWallpaperItem(items[index])
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BingWallPaperItemLoading() {
    SkeletonLoader(
        modifier = Modifier
            .aspectRatio(16f / 9f)
            .clip(MaterialTheme.shapes.large),
    )
}

@Composable
private fun BingWallpaperItem(bingWallpaperModel: BingWallpaperModel?) {
    if (bingWallpaperModel == null) return

    var showInfo by remember { mutableStateOf(false) }

    Column {
        AnimatedVisibility(
            visible = showInfo,
            enter = expandVertically(
                animationSpec = widgetEnter(visibilityThreshold = IntSize.VisibilityThreshold),
                expandFrom = Alignment.Top,
            ),
            exit = shrinkVertically(
                animationSpec = widgetExit(visibilityThreshold = IntSize.VisibilityThreshold),
                shrinkTowards = Alignment.Top,
            ),
        ) {
            Text(
                text = bingWallpaperModel.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        SkeletonAsyncImage(
            model = bingWallpaperModel.imageUrl,
            contentDescription = "",
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.large)
                .clickable {
                    showInfo = showInfo.not()
                },
        )
        AnimatedVisibility(
            visible = showInfo,
            enter = expandVertically(
                animationSpec = widgetEnter(visibilityThreshold = IntSize.VisibilityThreshold),
            ),
            exit = shrinkVertically(
                animationSpec = widgetExit(visibilityThreshold = IntSize.VisibilityThreshold),
            ),
        ) {
            Text(
                text = bingWallpaperModel.copyright,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@DevicePreviews
@Composable
private fun BingWallpaperScreenPreview() {
    AppPreview {
        val previewData = listOf(
            BingWallpaperModel(
                imageUrl = "1",
                copyright = "copyrightcopyrightcopyrightcopyrightcopyrightcopyrightcopyrightcopyrightcopyright",
                title = "titletitletitletitletitletitletitletitletitletitletitletitletitletitle",
                startDate = "2024-01-02",
            ),
            BingWallpaperModel(
                imageUrl = "2",
                copyright = "copyrightcopyrightcopyrightcopyrightcopyrightcopyrightcopyrightcopyrightcopyright",
                title = "titletitletitletitletitletitletitletitletitletitletitletitletitletitle",
                startDate = "2024-01-01",
            ),
        )
        val pagingData = PagingData.from(
            data = previewData,
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false),
            ),
        )

        BingWallpaperScreen(
            onBackClick = {},
            sourceFlow = flow { emit(pagingData) },
        )
    }
}
