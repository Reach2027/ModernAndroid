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

import android.net.Uri
import android.os.Build
import android.os.SystemClock
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.reach.base.android.common.util.getReadImagePermission
import com.reach.base.ui.common.devicepreview.previewWindowSizeClass
import com.reach.base.ui.common.toDp
import com.reach.base.ui.common.widget.AsyncLocalImage
import com.reach.base.ui.common.widget.VerticalTransparentBg
import com.reach.modernandroid.core.ui.common.AppPreview
import com.reach.modernandroid.core.ui.common.permission.RequestPermissionsScreen
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.core.ui.common.state.StatusDarkMode
import com.reach.modernandroid.core.ui.common.widget.AppTopBarWithBack
import com.reach.modernandroid.core.ui.design.AppIcons
import com.reach.modernandroid.core.ui.design.animation.AppAniSpec
import com.reach.modernandroid.core.ui.design.animation.groupEnter
import com.reach.modernandroid.core.ui.design.animation.groupExit
import com.reach.modernandroid.feature.album.navigation.ROUTE_ALBUM_PREVIEW
import com.reach.modernandroid.feature.data.album.model.LocalAlbumModel
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject
import kotlin.math.max

private const val TYPE_PADDING_TOP = 0x01
private const val TYPE_ITEM = 0x11
private const val TYPE_PADDING_BOTTOM = 0x21

@Composable
internal fun AlbumRoute(
    appUiState: AppUiState = koinInject(),
    viewModel: AlbumViewModel = koinNavViewModel(),
) {
    val previewIndex by viewModel.previewIndex.collectAsStateWithLifecycle()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RequestPermissionsScreen(
        permissions = listOf(getReadImagePermission()),
        requestTitle = R.string.album_request_permission,
        onBackClick = { appUiState.getNavController().navigateUp() },
        grantedCallback = { viewModel.getLocalAlbums() },
    ) {
        AlbumScreen(
            onBackClick = { appUiState.getNavController().navigateUp() },
            onStatusDarkModeSet = { appUiState.setStatusDarkMode(it) },
            onAlbumClick = { viewModel.changeAlbum(it) },
            onImageClick = {
                viewModel.setPreViewIndex(it)
                appUiState.getNavController().navigate(ROUTE_ALBUM_PREVIEW)
            },
            windowSizeClass = appUiState.getWindowSizeClass(),
            localImages = viewModel.localImages,
            previewIndex = previewIndex,
            uiState = uiState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumScreen(
    onBackClick: () -> Unit,
    onStatusDarkModeSet: (StatusDarkMode) -> Unit,
    onAlbumClick: (LocalAlbumModel) -> Unit,
    onImageClick: (Int) -> Unit,
    windowSizeClass: WindowSizeClass,
    localImages: Flow<PagingData<LocalImageModel>>,
    previewIndex: Int,
    uiState: AlbumUiState,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var showAlbumSelector by rememberSaveable { mutableStateOf(false) }
    BackHandler(showAlbumSelector) {
        showAlbumSelector = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LocalImage(
            onImageClick = onImageClick,
            scrollBehavior = scrollBehavior,
            windowSizeClass = windowSizeClass,
            previewIndex = previewIndex,
            localImages = localImages,
            showAlbumSelector = showAlbumSelector,
        )

        LocalAlbum(
            onAlbumClick = {
                showAlbumSelector = false
                onAlbumClick(it)
            },
            showAlbumSelector = showAlbumSelector,
            localAlbums = uiState.localAlbums,
        )

        TopBar(
            onBackClick = onBackClick,
            onStatusDarkModeSet = onStatusDarkModeSet,
            scrollBehavior = scrollBehavior,
            onAlbumSelectorShow = { showAlbumSelector = it },
            loadAlbumFinished = uiState.localAlbums.isNotEmpty(),
            showAlbumSelector = showAlbumSelector,
            currentAlbum = uiState.currentAlbum,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackClick: () -> Unit,
    onStatusDarkModeSet: (StatusDarkMode) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    loadAlbumFinished: Boolean,
    onAlbumSelectorShow: (Boolean) -> Unit,
    showAlbumSelector: Boolean,
    currentAlbum: LocalAlbumModel,
) {
    val topBarContentBeWhite by remember {
        derivedStateOf { scrollBehavior.state.overlappedFraction > 0.3f }
    }

    val arrowRotate by animateFloatAsState(
        targetValue = if (showAlbumSelector) -180f else 0f,
        label = "",
    )

    DisposableEffect(topBarContentBeWhite) {
        if (topBarContentBeWhite) {
            onStatusDarkModeSet(StatusDarkMode.Dark)
        } else {
            onStatusDarkModeSet(StatusDarkMode.FollowTheme)
        }

        onDispose { onStatusDarkModeSet(StatusDarkMode.FollowTheme) }
    }

    Box {
        if (topBarContentBeWhite) {
            VerticalTransparentBg(modifier = Modifier.height(TopAppBarDefaults.TopAppBarExpandedHeight))
        }
        AppTopBarWithBack(
            title = {
                Row(
                    modifier = Modifier
                        .width(intrinsicSize = IntrinsicSize.Max)
                        .clickable {
                            if (loadAlbumFinished) {
                                onAlbumSelectorShow(showAlbumSelector.not())
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (currentAlbum.albumId.isEmpty() && currentAlbum.albumName.isEmpty()) {
                            stringResource(id = R.string.album_all_photos)
                        } else {
                            currentAlbum.albumName
                        },
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (loadAlbumFinished) {
                        Icon(
                            imageVector = AppIcons.ExpandMore,
                            contentDescription = "",
                            modifier = Modifier
                                .rotate(arrowRotate)
                                .padding(4.dp),
                        )
                    }
                }
            },
            onBackClick = onBackClick,
            colors = if (topBarContentBeWhite) {
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                )
            } else {
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                )
            },
            scrollBehavior = scrollBehavior,
        )
    }
}

@Composable
private fun LocalAlbum(
    onAlbumClick: (LocalAlbumModel) -> Unit,
    showAlbumSelector: Boolean,
    localAlbums: List<LocalAlbumModel>,
) {
    val topBarH = WindowInsets.systemBars.getTop(LocalDensity.current).toDp() + 64.dp

    AnimatedVisibility(
        visible = showAlbumSelector,
        enter = fadeIn(animationSpec = groupEnter()) + slideInVertically(
            animationSpec = groupEnter(visibilityThreshold = IntOffset.VisibilityThreshold),
            initialOffsetY = { -it },
        ),
        exit = fadeOut(animationSpec = groupExit()) + slideOutVertically(
            animationSpec = groupExit(
                stiffness = AppAniSpec.STIFFNESS_GROUP_EXIT_LOW,
                visibilityThreshold = IntOffset.VisibilityThreshold,
            ),
            targetOffsetY = { -it },
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background.copy(
                        alpha = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                            0.6f
                        } else {
                            0.98f
                        },
                    ),
                )
                .padding(top = topBarH),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(420.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(
                    items = localAlbums,
                    key = { it.albumId },
                ) {
                    LocalAlbumItem(onAlbumClick = onAlbumClick, localAlbumModel = it)
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun LocalAlbumItem(
    onAlbumClick: (LocalAlbumModel) -> Unit,
    localAlbumModel: LocalAlbumModel,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onAlbumClick(localAlbumModel) },
    ) {
        AsyncLocalImage(
            model = localAlbumModel.coverUri,
            contentDescription = "",
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .size(96.dp),
            contentScale = ContentScale.Crop,
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = if (localAlbumModel.albumId.isEmpty() && localAlbumModel.albumName.isEmpty()) {
                    stringResource(id = R.string.album_all_photos)
                } else {
                    localAlbumModel.albumName
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = localAlbumModel.count.toString(),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalImage(
    onImageClick: (Int) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    windowSizeClass: WindowSizeClass,
    previewIndex: Int,
    localImages: Flow<PagingData<LocalImageModel>>,
    showAlbumSelector: Boolean,
) {
    val fixedCount = rememberColumnCount(windowSizeClass = windowSizeClass)
    var columnCountIndex by rememberSaveable { mutableIntStateOf(1) }

    val items: LazyPagingItems<LocalImageModel> = localImages.collectAsLazyPagingItems()

    val scrollState: LazyGridState = rememberLazyGridState()
    LaunchedEffect(previewIndex) {
        val visibleSize = scrollState.layoutInfo.visibleItemsInfo.size
        val middleSize = visibleSize / fixedCount[columnCountIndex]

        if (previewIndex < scrollState.firstVisibleItemIndex) {
            scrollState.animateScrollToItem(max(previewIndex - middleSize, 0))
        } else if (previewIndex > scrollState.firstVisibleItemIndex + visibleSize) {
            scrollState.animateScrollToItem(max(previewIndex - middleSize, 0))
        }
    }

    val blur by animateDpAsState(
        targetValue = if (showAlbumSelector) 80.dp else 0.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = Dp.VisibilityThreshold,
        ),
        label = "",
    )

    var isRefreshing by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                items.refresh()
                val start = SystemClock.uptimeMillis()
                snapshotFlow { items.loadState.refresh }
                    .collect {
                        if (it is LoadState.NotLoading) {
                            val interval = SystemClock.uptimeMillis() - start
                            if (interval < 500L) {
                                delay(500L)
                            }
                            isRefreshing = false
                            this.cancel()
                        }
                    }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .blur(blur),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(fixedCount[columnCountIndex]),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = scrollState,
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) },
                contentType = TYPE_PADDING_TOP,
            ) {
                Spacer(
                    modifier = Modifier.height(
                        WindowInsets.systemBars.getTop(LocalDensity.current).toDp() + 64.dp,
                    ),
                )
            }
            when (items.loadState.refresh) {
                is LoadState.Loading -> {}

                is LoadState.Error -> {}

                is LoadState.NotLoading -> {
                    items(
                        count = items.itemCount,
                        key = items.itemKey { it.id },
                        contentType = { TYPE_ITEM },
                    ) { index ->
                        LocalImageItem(
                            onImageClick = { onImageClick(index) },
                            localImageModel = items[index],
                        )
                    }
                    item(
                        span = { GridItemSpan(maxLineSpan) },
                        contentType = TYPE_PADDING_BOTTOM,
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyGridItemScope.LocalImageItem(
    onImageClick: () -> Unit,
    localImageModel: LocalImageModel?,
) {
    if (localImageModel == null) return

    Box(modifier = Modifier.animateItem()) {
        AsyncLocalImage(
            model = localImageModel.uri,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable { onImageClick() },
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun rememberColumnCount(windowSizeClass: WindowSizeClass): IntArray {
    return remember(windowSizeClass) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> intArrayOf(1, 3, 4)
            WindowWidthSizeClass.Medium -> intArrayOf(2, 4, 5)
            else -> intArrayOf(3, 5, 6)
        }
    }
}

@Preview
@Composable
private fun AlbumScreenPreview() {
    AppPreview {
        val previewData = listOf(
            LocalImageModel(
                id = "1",
                uri = Uri.parse("a"),
                modifierTime = 0L,
                albumId = "11",
                albumName = "Album",
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
        AlbumScreen(
            onBackClick = {},
            onImageClick = {},
            onAlbumClick = {},
            onStatusDarkModeSet = {},
            windowSizeClass = previewWindowSizeClass(),
            localImages = flow { emit(pagingData) },
            previewIndex = 0,
            uiState = AlbumUiState(),
        )
    }
}
