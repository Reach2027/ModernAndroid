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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import com.reach.modernandroid.feature.album.AlbumViewModel
import com.reach.modernandroid.feature.album.ext.customDetectTransformGestures
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import com.reach.modernandroid.ui.core.common.AppPreview
import com.reach.modernandroid.ui.core.common.state.AppUiState
import com.reach.modernandroid.ui.core.common.widget.AppTopBarWithBack
import com.reach.modernandroid.ui.core.design.animation.widgetEnter
import com.reach.ui.base.common.widget.AsyncLocalImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlin.math.max
import kotlin.math.min

private const val MAX_SHOW_SCALE = 5f
private const val MAX_SCALE = 5.4f

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

    LifecycleStartEffect(true) {
        setFullScreen(fullScreen)
        onStopOrDispose {
            setFullScreen(false)
        }
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

        TopBar(
            onBackClick = onBackClick,
            fullScreen = fullScreen,
            previewIndex = previewIndex,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackClick: () -> Unit,
    fullScreen: Boolean,
    previewIndex: Int,
) {
    var showTopBar by remember { mutableStateOf(false) }
    LaunchedEffect(previewIndex) {
        delay(400L)
        showTopBar = true
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
        AppTopBarWithBack(
            title = { },
            onBackClick = onBackClick,
            colors = TopAppBarDefaults.topAppBarColors(),
        )
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

    val scaleState = remember { mutableFloatStateOf(1f) }
    LaunchedEffect(localImageModel.id) {
        snapshotFlow { scaleState.floatValue }.collectLatest {
            if (it > 1f && fullScreen.not()) {
                previewImage()
            }
        }
    }

    val offsetState = remember { mutableStateOf(Offset.Zero) }

    var aniFlag by remember { mutableStateOf(Pair(false, false)) }
    val scaleAni by animateFloatAsState(
        targetValue = scaleState.floatValue,
        animationSpec = defaultSpring(),
        label = "",
    ) { aniFlag = aniFlag.copy(first = false) }
    val offsetAni by animateOffsetAsState(
        targetValue = offsetState.value,
        animationSpec = defaultSpring(visibilityThreshold = Offset.VisibilityThreshold),
        label = "",
    ) { aniFlag = aniFlag.copy(second = false) }

    var downPointerCount by remember { mutableIntStateOf(0) }

    var painterSize by remember { mutableStateOf(Size.Zero) }
    var boxSize by remember { mutableStateOf(Size.Zero) }
    var showSize by remember { mutableStateOf(Size.Zero) }

    val maxOffsetState = remember { mutableStateOf(Offset.Zero) }

    val consumeTransform = remember { mutableStateOf(true) }

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
                        val targetScale: Float
                        val targetOffset: Offset
                        if (scaleState.floatValue == 1f) {
                            val fullScreenScale = max(
                                boxSize.width / showSize.width,
                                boxSize.height / showSize.height,
                            )
                            targetScale = max(fullScreenScale, 2f)
                            targetOffset = Offset(
                                boxSize.width / 2f - it.x,
                                boxSize.height / 2f - it.y,
                            ) * ((targetScale - 1f) / 2f + 1f)
                        } else {
                            targetScale = 1f
                            targetOffset = Offset.Zero
                        }
                        aniFlag = Pair(true, true)

                        updateScale(
                            scale = targetScale,
                            scaleState = scaleState,
                            showSize = showSize,
                            boxSize = boxSize,
                            maxOffsetState = maxOffsetState,
                        )
                        updateOffset(
                            scale = targetScale,
                            targetOffset = targetOffset,
                            offsetState = offsetState,
                            maxOffset = maxOffsetState.value,
                            consumeTransform = consumeTransform,
                        )
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

                        if (downPointerCount == 0) {
//                            if (offsetState.value.y > maxOffsetState.value.y) {
//                                exitPreview()
//                            } else
                            if (scaleState.floatValue < 1f && scaleState.floatValue > 0.7f) {
                                aniFlag = Pair(true, true)
                                updateScale(
                                    scale = 1f,
                                    scaleState = scaleState,
                                    showSize = showSize,
                                    boxSize = boxSize,
                                    maxOffsetState = maxOffsetState,
                                )
                                updateOffset(
                                    scale = 1f,
                                    targetOffset = Offset.Zero,
                                    offsetState = offsetState,
                                    maxOffset = maxOffsetState.value,
                                    consumeTransform = consumeTransform,
                                )
                            } else if (scaleState.floatValue <= 0.7f) {
                                exitPreview()
                            } else if (scaleState.floatValue > MAX_SHOW_SCALE) {
                                aniFlag = Pair(true, false)
                                updateScale(
                                    scale = MAX_SHOW_SCALE,
                                    scaleState = scaleState,
                                    showSize = showSize,
                                    boxSize = boxSize,
                                    maxOffsetState = maxOffsetState,
                                )
                            }
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                customDetectTransformGestures(consume = consumeTransform) { _, pan, zoom, _ ->
                    if (zoom != 1f) {
                        val scale = min(scaleState.floatValue * zoom, MAX_SCALE)
                        updateScale(
                            scale = scale,
                            scaleState = scaleState,
                            showSize = showSize,
                            boxSize = boxSize,
                            maxOffsetState = maxOffsetState,
                        )
                    }
                    updateOffset(
                        scale = scaleState.floatValue,
                        targetOffset = offsetState.value + pan / zoom,
                        offsetState = offsetState,
                        maxOffset = maxOffsetState.value,
                        consumeTransform = consumeTransform,
                    )
                }
            }
            .graphicsLayer {
                if (aniFlag.first) {
                    scaleX = scaleAni
                    scaleY = scaleAni
                } else {
                    scaleX = scaleState.floatValue
                    scaleY = scaleState.floatValue
                }
                var offsetY: Float
                if (aniFlag.second) {
                    translationX = offsetAni.x
                    offsetY = offsetAni.y
                } else {
                    translationX = offsetState.value.x
                    offsetY = offsetState.value.y
                }
                translationY = offsetY

                if (aniFlag.second.not() && downPointerCount == 0) {
                    if (offsetY > maxOffsetState.value.y) {
                        offsetY = maxOffsetState.value.y
                        aniFlag = Pair(false, true)
                        offsetState.value = Offset(translationX, offsetY)
                    } else if (offsetY < -maxOffsetState.value.y) {
                        offsetY = -maxOffsetState.value.y
                        aniFlag = Pair(false, true)
                        offsetState.value = Offset(translationX, offsetY)
                    }
                }
            },
        contentScale = ContentScale.Fit,
    )
}

private fun updateScale(
    scale: Float,
    scaleState: MutableState<Float>,
    showSize: Size,
    boxSize: Size,
    maxOffsetState: MutableState<Offset>,
) {
    scaleState.value = scale
    val currentSize = showSize * scale
    maxOffsetState.value = Offset(
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

private fun updateOffset(
    scale: Float,
    targetOffset: Offset,
    offsetState: MutableState<Offset>,
    maxOffset: Offset,
    consumeTransform: MutableState<Boolean>,
) {
    val offset = if (scale > 1f) {
        val consumeT: Boolean
        val offsetX = if (targetOffset.x >= maxOffset.x) {
            consumeT = false
            maxOffset.x
        } else if (targetOffset.x <= -maxOffset.x) {
            consumeT = false
            -maxOffset.x
        } else {
            consumeT = true
            targetOffset.x
        }
        consumeTransform.value = consumeT
        Offset(offsetX, targetOffset.y)
    } else if (scale == 1f) {
        consumeTransform.value = false
        Offset(0f, targetOffset.y)
    } else {
        consumeTransform.value = true
        targetOffset
    }
    offsetState.value = offset
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
