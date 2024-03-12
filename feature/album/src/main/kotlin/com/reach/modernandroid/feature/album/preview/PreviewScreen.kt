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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import com.reach.base.ui.common.widget.SkeletonAsyncImage
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.core.ui.common.widget.AppTopBarWithBack
import com.reach.modernandroid.core.ui.design.animation.widgetEnter
import com.reach.modernandroid.feature.album.AlbumViewModel
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun PreviewRoute(
    viewModel: AlbumViewModel,
    appUiState: AppUiState,
) {
    val index by viewModel.previewIndex.collectAsStateWithLifecycle()

    PreviewScreen(
        onBackClick = { appUiState.getNavController().navigateUp() },
        onIndexChange = { viewModel.setPreViewIndex(it) },
        setFullScreen = { appUiState.setFullScreen(it) },
        localImages = viewModel.localImages,
        index = index,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun PreviewScreen(
    onBackClick: () -> Unit,
    onIndexChange: (Int) -> Unit,
    setFullScreen: (Boolean) -> Unit,
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
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                setFullScreen(fullScreen)
            } else if (event == Lifecycle.Event.ON_STOP) {
                setFullScreen(false)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSpacing = 32.dp,
            userScrollEnabled = false,
        ) { index ->
            PreviewItem(
                onImageClick = {
                    fullScreen = fullScreen.not()
                    setFullScreen(fullScreen)
                },
                previewImage = {
                    fullScreen = true
                    setFullScreen(true)
                },
                exitPreview = onBackClick,
                localImageModel = items[index],
                fullScreen = fullScreen,
                pagerState = pagerState,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PreviewItem(
    onImageClick: () -> Unit,
    previewImage: () -> Unit,
    exitPreview: () -> Unit,
    localImageModel: LocalImageModel?,
    fullScreen: Boolean,
    pagerState: PagerState,
) {
    if (localImageModel == null) {
        return
    }

    var scale: Float by remember(localImageModel) { mutableFloatStateOf(1f) }
    val scaleAni by animateFloatAsState(targetValue = scale, label = "")
    LaunchedEffect(localImageModel) {
        snapshotFlow { scale }.collectLatest {
            if (scale > 1f && fullScreen.not()) {
                previewImage()
            }
        }
    }

    val previewBg by animateColorAsState(
        targetValue = if (fullScreen) Color.Black else Color.Transparent,
        label = "",
    )

    var offset: Offset by remember(localImageModel) { mutableStateOf(Offset.Zero) }
    val offsetAni by animateOffsetAsState(targetValue = offset, label = "")

    val state = rememberTransformableState { zoomChange, panChange, _ ->
        scale *= zoomChange
        offset += panChange
    }

    var downPointerCount by remember { mutableIntStateOf(0) }

    var painterSize by remember { mutableStateOf(Size.Zero) }

    var boxSize by remember { mutableStateOf(Size.Zero) }

    var showSize by remember { mutableStateOf(Size.Zero) }

    var maxOffset by remember { mutableStateOf(Offset.Zero) }

    var overOffsetX by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(painterSize, boxSize) {
        if (painterSize == Size.Zero || boxSize == Size.Zero) {
            return@LaunchedEffect
        }
        val scaleX = painterSize.width / boxSize.width
        val scaleY = painterSize.height / boxSize.height
        showSize = if (scaleX > scaleY) {
            Size(boxSize.width, painterSize.height / painterSize.width * boxSize.width)
        } else {
            Size(painterSize.width / painterSize.height * boxSize.height, boxSize.height)
        }
    }

    LaunchedEffect(showSize, scale) {
        val currentSize = showSize * scale
        maxOffset = Offset(
            x = if (currentSize.width <= boxSize.width) {
                0f
            } else {
                (currentSize.width - boxSize.width) / 2f
            },
            y = if (currentSize.height <= boxSize.height) {
                0f
            } else {
                (currentSize.height - boxSize.height) / 2f
            },
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = previewBg)
            .onSizeChanged { boxSize = it.toSize() },
    ) {
        SkeletonAsyncImage(
            model = localImageModel.uri,
            onState = {
                if (it is AsyncImagePainter.State.Success) {
                    painterSize = it.painter.intrinsicSize
                }
            },
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .transformable(state)
                .graphicsLayer {
                    scaleX = scaleAni
                    scaleY = scaleAni

                    if ((scale > 1f && maxOffset != Offset.Zero)
                        || (scale <= 1f && maxOffset == Offset.Zero)
                    ) {
                        val nX = if (offset.x > maxOffset.x) {
                            overOffsetX = maxOffset.x - offset.x
                            maxOffset.x
                        } else if (offset.x < -maxOffset.x) {
                            overOffsetX = maxOffset.x - offset.x
                            -maxOffset.x
                        } else {
                            offset.x
                        }

                        val nY = if (offset.y > maxOffset.y) {
                            maxOffset.y
                        } else if (offset.y < -maxOffset.y) {
                            -maxOffset.y
                        } else {
                            offset.y
                        }
                        offset = Offset(nX, nY)
                    }

                    translationX = offsetAni.x
                    translationY = offsetAni.y
                }
                .pointerInput(localImageModel) {
                    detectTapGestures(
                        onTap = { onImageClick() },
                        onDoubleTap = {
                            if (scale == 1f) {
                                scale = 2f
                                offset =
                                    Offset(boxSize.width / 2f - it.x, boxSize.height / 2f - it.y)
                            } else {
                                scale = 1f
                                offset = Offset.Zero
                            }
                        },
                    )
                }
                .pointerInput(localImageModel) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Press) {
                                downPointerCount++
                            } else if (event.type == PointerEventType.Release) {
                                downPointerCount--
                            }

                            if (scale < 1f && scale > 0.6f && downPointerCount == 0) {
                                scale = 1f
                                offset = Offset.Zero
                            } else if (scale <= 0.6f && downPointerCount == 0) {
                                exitPreview()
                            }
                        }
                    }
                },
            placeHolderModifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}
