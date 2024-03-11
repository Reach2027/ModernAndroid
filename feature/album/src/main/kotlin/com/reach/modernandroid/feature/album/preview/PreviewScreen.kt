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

package com.reach.modernandroid.feature.album.preview

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import com.reach.base.ui.common.widget.AsyncLocalImage
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.core.ui.common.widget.AppTopBarWithBack
import com.reach.modernandroid.core.ui.design.animation.widgetEnter
import com.reach.modernandroid.feature.album.AlbumViewModel
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import kotlinx.coroutines.flow.Flow

@Composable
internal fun PreviewRoute(
    viewModel: AlbumViewModel,
    appUiState: AppUiState,
) {
    val index by viewModel.previewIndex.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                appUiState.setFullScreen(false)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    PreviewScreen(
        onBackClick = { appUiState.getNavController().navigateUp() },
        onIndexChange = { viewModel.setPreViewIndex(it) },
        onFullScreenChange = { appUiState.setFullScreen(it) },
        localImages = viewModel.localImages,
        index = index,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun PreviewScreen(
    onBackClick: () -> Unit,
    onIndexChange: (Int) -> Unit,
    onFullScreenChange: (Boolean) -> Unit,
    localImages: Flow<PagingData<LocalImageModel>>,
    index: Int,
) {
    val items: LazyPagingItems<LocalImageModel> = localImages.collectAsLazyPagingItems()

    val pagerState = rememberPagerState(initialPage = index) {
        items.itemCount
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            onIndexChange(it)
        }
    }

    var fullScreen by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) {
            PreviewItem(
                onImageClick = {
                    fullScreen = fullScreen.not()
                    onFullScreenChange(fullScreen)
                },
                localImageModel = items[it],
            )
        }

        AnimatedVisibility(
            visible = fullScreen.not(),
            enter = fadeIn(animationSpec = widgetEnter()) + slideInVertically(
                animationSpec = widgetEnter(visibilityThreshold = IntOffset.VisibilityThreshold),
                initialOffsetY = { -it },
            ),
            exit = fadeOut(animationSpec = widgetEnter()) + slideOutVertically(
                animationSpec = widgetEnter(visibilityThreshold = IntOffset.VisibilityThreshold),
                targetOffsetY = { -it },
            ),
        ) {
            Box(modifier = Modifier.height(IntrinsicSize.Max)) {
                AppTopBarWithBack(
                    title = { },
                    onBackClick = onBackClick,
                    colors = TopAppBarDefaults.topAppBarColors(),
                )
            }
        }
    }
}

@Composable
private fun PreviewItem(
    onImageClick: () -> Unit,
    localImageModel: LocalImageModel?,
) {
    if (localImageModel == null) {
        return
    }

    var scale: Float by remember { mutableFloatStateOf(1f) }
    val scaleAni by animateFloatAsState(targetValue = scale, label = "")

    var offset: Offset by remember { mutableStateOf(Offset.Zero) }
    val offsetAni by animateIntOffsetAsState(
        targetValue = IntOffset(
            offset.x.toInt(),
            offset.y.toInt(),
        ),
        label = "",
    )

    AsyncLocalImage(
        model = localImageModel.uri,
        onState = {
            if (it is AsyncImagePainter.State.Success) {
                Log.e("REACH", "PreviewItem: ${it.painter.intrinsicSize}")
            }
        },
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .scale(scaleAni)
            .offset { offsetAni }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onImageClick() },
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    offset += pan
                }
            },
        contentScale = ContentScale.Fit,
    )
}