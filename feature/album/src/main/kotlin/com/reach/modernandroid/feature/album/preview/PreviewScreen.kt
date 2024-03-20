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

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
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
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import com.reach.base.ui.common.widget.AsyncLocalImage
import com.reach.modernandroid.core.ui.common.AppPreview
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.core.ui.common.widget.AppTopBarWithBack
import com.reach.modernandroid.core.ui.design.animation.widgetEnter
import com.reach.modernandroid.feature.album.AlbumViewModel
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val MAX_SHOW_SCALE = 5f
private const val MAX_SCALE = 5.3f

@Composable
internal fun PreviewRoute(
    viewModel: AlbumViewModel,
    appUiState: AppUiState,
) {
    val previewIndex by viewModel.previewIndex.collectAsStateWithLifecycle()

    PreviewScreen(
        onBackClick = { appUiState.getNavController().navigateUp() },
        onIndexChange = { viewModel.setPreViewIndex(it) },
        setFullScreen = { appUiState.setFullScreen(it) },
        localImages = viewModel.localImages,
        previewIndex = previewIndex,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun PreviewScreen(
    onBackClick: () -> Unit,
    onIndexChange: (Int) -> Unit,
    setFullScreen: (Boolean) -> Unit,
    localImages: Flow<PagingData<LocalImageModel>>,
    previewIndex: Int,
) {
    val items: LazyPagingItems<LocalImageModel> = localImages.collectAsLazyPagingItems()

    val pagerState = rememberPagerState(initialPage = previewIndex) { items.itemCount }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            onIndexChange(it)
        }
    }

    var fullScreen by rememberSaveable { mutableStateOf(false) }
    var showTopBar by remember { mutableStateOf(false) }
    LaunchedEffect(previewIndex) {
        delay(400L)
        showTopBar = true
    }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (fullScreen) Color.Black else Color.Transparent),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSpacing = 32.dp,
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
            )
        }

        AnimatedVisibility(
            visible = fullScreen.not() && showTopBar,
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

@SuppressLint("ReturnFromAwaitPointerEventScope")
@Composable
private fun PreviewItem(
    onImageClick: () -> Unit,
    previewImage: () -> Unit,
    exitPreview: () -> Unit,
    localImageModel: LocalImageModel?,
    fullScreen: Boolean,
) {
    if (localImageModel == null) {
        return
    }

    val previewBg by animateColorAsState(
        targetValue = if (fullScreen) Color.Black else Color.Transparent,
        label = "",
    )

    var scale: Float by remember { mutableFloatStateOf(1f) }
    val scaleAni by animateFloatAsState(
        targetValue = scale,
        animationSpec = defaultSpring(),
        label = "",
    )
    LaunchedEffect(localImageModel.id) {
        snapshotFlow { scale }.collectLatest {
            if (scale > 1f && fullScreen.not()) {
                previewImage()
            }
        }
    }

    var offset: Offset by remember { mutableStateOf(Offset.Zero) }
    val offsetAni by animateOffsetAsState(
        targetValue = offset,
        animationSpec = defaultSpring(visibilityThreshold = Offset.VisibilityThreshold),
        label = "",
    )

    var downPointerCount by remember { mutableIntStateOf(0) }

    var painterSize by remember { mutableStateOf(Size.Zero) }

    var boxSize by remember { mutableStateOf(Size.Zero) }

    var showSize by remember { mutableStateOf(Size.Zero) }

    var maxOffset by remember { mutableStateOf(Offset.Zero) }

    val consumeHorizontalOffset = remember { mutableStateOf(true) }

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

    AsyncLocalImage(
        model = localImageModel.uri,
        onState = {
            if (it is AsyncImagePainter.State.Success) {
                painterSize = it.painter.intrinsicSize
            }
        },
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .background(color = previewBg)
            .onSizeChanged { boxSize = it.toSize() }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onImageClick() },
                    onDoubleTap = {
                        if (scale == 1f) {
                            val fullScreenScale = max(
                                boxSize.width / showSize.width,
                                boxSize.height / showSize.height,
                            )
                            scale = max(fullScreenScale, 2f)
                            offset = Offset(
                                boxSize.width / 2f - it.x,
                                boxSize.height / 2f - it.y,
                            ) * ((scale - 1f) / 2f + 1f)
                        } else {
                            scale = 1f
                            offset = Offset.Zero
                        }
                    },
                )
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.type == PointerEventType.Press) {
                            downPointerCount++
                        } else if (event.type == PointerEventType.Release) {
                            downPointerCount--
                        }

                        if (downPointerCount == 0 && scale < 1f && scale > 0.6f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else if (downPointerCount == 0 && scale <= 0.6f) {
                            exitPreview()
                        } else if (downPointerCount == 0 && scale > MAX_SHOW_SCALE) {
                            scale = MAX_SHOW_SCALE
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                customDetectTransformGestures(consume = consumeHorizontalOffset) { _, pan, zoom, _ ->
                    scale *= zoom
                    scale = min(scale, MAX_SCALE)
                    offset += pan * zoom
                }
            }
            .graphicsLayer {
                scaleX = scaleAni
                scaleY = scaleAni

                if ((scale > 1f && maxOffset != Offset.Zero) ||
                    (scale <= 1f && maxOffset == Offset.Zero)
                ) {
                    val offsetX = if (offset.x >= maxOffset.x) {
                        consumeHorizontalOffset.value = false
                        maxOffset.x
                    } else if (offset.x <= -maxOffset.x) {
                        consumeHorizontalOffset.value = false
                        -maxOffset.x
                    } else {
                        consumeHorizontalOffset.value = true
                        offset.x
                    }

                    val offsetY = if (offset.y >= maxOffset.y) {
                        maxOffset.y
                    } else if (offset.y <= -maxOffset.y) {
                        -maxOffset.y
                    } else {
                        offset.y
                    }
                    offset = Offset(offsetX, offsetY)
                }

                translationX = offsetAni.x
                translationY = offsetAni.y
            },
//        placeHolderModifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
    )
}

private suspend fun PointerInputScope.customDetectTransformGestures(
    consume: State<Boolean>,
    panZoomLock: Boolean = false,
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float) -> Unit,
) {
    awaitEachGesture {
        var rotation = 0f
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop
        var lockedToPanZoom = false

        awaitFirstDown(requireUnconsumed = false)
        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.fastAny { it.isConsumed }
            if (!canceled) {
                val zoomChange = event.calculateZoom()
                val rotationChange = event.calculateRotation()
                val panChange = event.calculatePan()

                if (!pastTouchSlop) {
                    zoom *= zoomChange
                    rotation += rotationChange
                    pan += panChange

                    val centroidSize = event.calculateCentroidSize(useCurrent = false)
                    val zoomMotion = abs(1 - zoom) * centroidSize
                    val rotationMotion = abs(rotation * PI.toFloat() * centroidSize / 180f)
                    val panMotion = pan.getDistance()

                    if (zoomMotion > touchSlop ||
                        rotationMotion > touchSlop ||
                        panMotion > touchSlop
                    ) {
                        pastTouchSlop = true
                        lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
                    }
                }

                if (pastTouchSlop) {
                    val centroid = event.calculateCentroid(useCurrent = false)
                    val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
                    if (effectiveRotation != 0f ||
                        zoomChange != 1f ||
                        panChange != Offset.Zero
                    ) {
                        onGesture(centroid, panChange, zoomChange, effectiveRotation)
                    }
                    if (consume.value) {
                        event.changes.fastForEach {
                            if (it.positionChanged()) {
                                it.consume()
                            }
                        }
                    }
                }
            }
        } while (!canceled && event.changes.fastAny { it.pressed })
    }
}

@Stable
private fun <T> defaultSpring(
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    stiffness: Float = Spring.StiffnessMediumLow,
    visibilityThreshold: T? = null,
): SpringSpec<T> = spring(
    dampingRatio = dampingRatio,
    stiffness = stiffness,
    visibilityThreshold = visibilityThreshold,
)

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
        PreviewScreen(
            onBackClick = { },
            onIndexChange = { },
            setFullScreen = { },
            localImages = flow { emit(pagingData) },
            previewIndex = 0,
        )
    }
}
