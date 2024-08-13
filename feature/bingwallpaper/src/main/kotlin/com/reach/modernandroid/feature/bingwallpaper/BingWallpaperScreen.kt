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

package com.reach.modernandroid.feature.bingwallpaper

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.reach.modernandroid.feature.data.bingwallpaper.model.BingWallpaperModel
import com.reach.modernandroid.ui.core.common.AppPreview
import com.reach.modernandroid.ui.core.common.navigation.AppRoute
import com.reach.modernandroid.ui.core.common.state.AppUiState
import com.reach.modernandroid.ui.core.common.state.StatusDarkMode
import com.reach.modernandroid.ui.core.common.widget.AppTopBarWithBack
import com.reach.modernandroid.ui.core.design.animation.widgetEnter
import com.reach.modernandroid.ui.core.design.animation.widgetExit
import com.reach.ui.base.common.devicepreview.DevicePreviews
import com.reach.ui.base.common.toDp
import com.reach.ui.base.common.widget.SkeletonAsyncImage
import com.reach.ui.base.common.widget.SkeletonLoader
import com.reach.ui.base.common.widget.VerticalTransparentBg
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

private const val RATIO_16_9 = 16f / 9f

fun NavGraphBuilder.bingWallpaperRoute() {
    composable<AppRoute.BingWallpaper> {
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
        onStatusDarkModeSet = { appUiState.setStatusDarkMode(it) },
        bingWallPapers = viewModel.bingWallpapers,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BingWallpaperScreen(
    onBackClick: () -> Unit,
    onStatusDarkModeSet: (StatusDarkMode) -> Unit,
    bingWallPapers: Flow<PagingData<BingWallpaperModel>>,
) {
    val items: LazyPagingItems<BingWallpaperModel> = bingWallPapers.collectAsLazyPagingItems()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val statusContentBeWhite by remember {
        derivedStateOf {
            scrollBehavior.state.collapsedFraction > 0.9f
        }
    }

    DisposableEffect(statusContentBeWhite) {
        if (statusContentBeWhite) {
            onStatusDarkModeSet(StatusDarkMode.Dark)
        } else {
            onStatusDarkModeSet(StatusDarkMode.FollowTheme)
        }
        onDispose { onStatusDarkModeSet(StatusDarkMode.FollowTheme) }
    }

    val systemBarH = WindowInsets.systemBars.getTop(LocalDensity.current)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBarWithBack(
                title = { Text(text = stringResource(id = R.string.bing_wallpaper_title)) },
                onBackClick = onBackClick,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                windowInsets = WindowInsets(
                    0,
                    (systemBarH * (1f - scrollBehavior.state.collapsedFraction)).toInt(),
                    0,
                    0,
                ),
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
                        items(count = 8) {
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

                        if (items.itemCount > 0) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    HorizontalDivider()
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "这是底线",
                                        style = MaterialTheme.typography.labelMedium,
                                    )
                                    Spacer(modifier = Modifier.height(32.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        if (statusContentBeWhite) {
            VerticalTransparentBg(
                modifier = Modifier.height(systemBarH.toDp()),
            )
        }
    }
}

@Composable
private fun BingWallPaperItemLoading() {
    SkeletonLoader(
        modifier = Modifier
            .aspectRatio(RATIO_16_9)
            .clip(MaterialTheme.shapes.large),
    )
}

@Composable
private fun LazyStaggeredGridItemScope.BingWallpaperItem(bingWallpaperModel: BingWallpaperModel?) {
    if (bingWallpaperModel == null) return

    var showInfo by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.animateItem()) {
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
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(MaterialTheme.shapes.large)
                .clickable { showInfo = showInfo.not() },
            placeHolderModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(RATIO_16_9)
                .clip(MaterialTheme.shapes.large),
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
            onStatusDarkModeSet = {},
            bingWallPapers = flow { emit(pagingData) },
        )
    }
}
