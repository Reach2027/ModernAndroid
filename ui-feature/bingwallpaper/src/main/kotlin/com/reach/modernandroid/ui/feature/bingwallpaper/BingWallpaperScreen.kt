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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.reach.core.ui.common.SkeletonAsyncImage
import com.reach.core.ui.common.SkeletonLoader
import com.reach.modernandroid.data.feature.bingwallpaper.model.BingWallpaperModel
import com.reach.modernandroid.ui.base.common.AppUiState
import com.reach.modernandroid.ui.base.common.navigation.AppRoute
import com.reach.modernandroid.ui.base.common.navigation.screenComposable
import com.reach.modernandroid.ui.base.common.widget.AppTopBarWithBack
import com.reach.modernandroid.ui.base.resource.theme.AppTheme
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
        onBackClick = { appUiState.navController.navigateUp() },
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

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 400.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
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

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BingWallPaperItemLoading() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SkeletonLoader(
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.large),
        )
        Spacer(modifier = Modifier.height(4.dp))
        SkeletonLoader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(28.dp)
                .clip(MaterialTheme.shapes.small),
        )
    }
}

@Composable
private fun BingWallpaperItem(bingWallpaperModel: BingWallpaperModel?) {
    if (bingWallpaperModel == null) return

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SkeletonAsyncImage(
            model = bingWallpaperModel.imageUrl,
            contentDescription = "",
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.large),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = """${bingWallpaperModel.startDate}  ${bingWallpaperModel.title}""",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 32.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
private fun BingWallpaperScreenPreview() {
    AppTheme {
        val previewData = listOf(
            BingWallpaperModel(
                imageUrl = "",
                copyright = "",
                title = "titletitletitletitletitletitletitletitletitletitletitletitle",
                startDate = "2024-01-01",
            ),
        )
        val pagingData = PagingData.from(previewData)

        BingWallpaperScreen(
            onBackClick = {},
            sourceFlow = flow { emit(pagingData) },
        )
    }
}
