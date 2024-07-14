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

package com.reach.modernandroid.feature.me

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImagePainter
import com.reach.base.ui.common.devicepreview.DevicePreviews
import com.reach.base.ui.common.devicepreview.previewWindowSizeClass
import com.reach.base.ui.common.toDp
import com.reach.base.ui.common.widget.SkeletonAsyncImage
import com.reach.base.ui.common.widget.SkeletonLoader
import com.reach.base.ui.common.widget.VerticalTransparentBg
import com.reach.modernandroid.core.ui.common.AppPreview
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.core.ui.common.state.StatusDarkMode
import com.reach.modernandroid.core.ui.design.AppIcons
import com.reach.modernandroid.core.ui.design.animation.topDestEnterTransition
import com.reach.modernandroid.core.ui.design.animation.topDestExitTransition
import com.reach.modernandroid.core.ui.design.animation.widgetEnter
import com.reach.modernandroid.core.ui.design.animation.widgetExit
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

private const val RATIO_16_9 = 16f / 9f

fun NavGraphBuilder.meRoute() {
    composable<AppRoute.Me>(
        enterTransition = { topDestEnterTransition() },
        exitTransition = { topDestExitTransition() },
    ) {
        MeRoute()
    }
}

@Composable
private fun MeRoute(
    appUiState: AppUiState = koinInject(),
    viewModel: MeViewModel = koinNavViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MeScreen(
        onWallpaperClick = { appUiState.getNavController().navigate(AppRoute.BingWallpaper) },
        onSettingsClick = { appUiState.getNavController().navigate(AppRoute.Settings) },
        onStatusDarkModeSet = { appUiState.setStatusDarkMode(it) },
        windowSizeClass = appUiState.getWindowSizeClass(),
        uiState = uiState,
    )
}

@Composable
private fun MeScreen(
    onWallpaperClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onStatusDarkModeSet: (StatusDarkMode) -> Unit,
    windowSizeClass: WindowSizeClass,
    uiState: MeUiState,
) {
    var isExpanded by remember { mutableStateOf(false) }

    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ||
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
    ) {
        isExpanded = true
        MeScreenExpanded(
            uiState = uiState,
            onWallpaperClick = onWallpaperClick,
            onSettingsClick = onSettingsClick,
        )
    } else {
        isExpanded = false
        MeScreenCompat(
            uiState = uiState,
            onWallpaperClick = onWallpaperClick,
            onSettingsClick = onSettingsClick,
        )
    }

    LifecycleResumeEffect(true) {
        if (isExpanded) {
            onStatusDarkModeSet(StatusDarkMode.FollowTheme)
        } else {
            onStatusDarkModeSet(StatusDarkMode.Dark)
        }
        onPauseOrDispose {
            onStatusDarkModeSet(StatusDarkMode.FollowTheme)
        }
    }
}

@Composable
private fun MeScreenExpanded(
    uiState: MeUiState,
    onWallpaperClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val systemBarH = WindowInsets.systemBars.getTop(LocalDensity.current).toDp()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = systemBarH),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.large),
        ) {
            PersonInfo(
                uiState = uiState,
                onWallpaperClick = onWallpaperClick,
                onSettingsClick = onSettingsClick,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        DeviceInfo(uiState = uiState)
    }
}

@Composable
private fun MeScreenCompat(
    onWallpaperClick: () -> Unit,
    onSettingsClick: () -> Unit,
    uiState: MeUiState,
) {
    var showTransparentBg by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            PersonInfo(
                uiState = uiState,
                onWallpaperClick = onWallpaperClick,
                onSettingsClick = onSettingsClick,
                needShowTransparentBg = { showTransparentBg = true },
            )
            Spacer(modifier = Modifier.height(16.dp))
            DeviceInfo(uiState = uiState)
        }
        AnimatedVisibility(
            visible = uiState.isImageLoading.not() && showTransparentBg,
            enter = fadeIn(animationSpec = widgetEnter()),
            exit = fadeOut(animationSpec = widgetExit()),
        ) {
            VerticalTransparentBg(
                modifier = Modifier.height(
                    WindowInsets.systemBars
                        .getTop(LocalDensity.current)
                        .toDp(),
                ),
            )
        }
    }
}

@Composable
private fun PersonInfo(
    uiState: MeUiState,
    onWallpaperClick: () -> Unit,
    onSettingsClick: () -> Unit,
    needShowTransparentBg: (() -> Unit)? = null,
) {
    Box {
        if (uiState.isImageLoading) {
            SkeletonLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(RATIO_16_9),
            )
        } else {
            SkeletonAsyncImage(
                model = uiState.imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(onClick = onWallpaperClick),
                placeHolderModifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(RATIO_16_9),
                onState = { state ->
                    if (needShowTransparentBg == null) {
                        return@SkeletonAsyncImage
                    }
                    if (state is AsyncImagePainter.State.Success) {
                        needShowTransparentBg()
                    }
                },
            )
        }

        Icon(
            imageVector = AppIcons.Settings,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clickable { onSettingsClick() }
                .padding(16.dp),
            tint = Color.White,
        )
    }
}

@Composable
private fun DeviceInfo(uiState: MeUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(
                id = R.string.me_network_available,
                uiState.isNetworkAvailable.toString(),
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.me_network_type, uiState.networkType.name))
    }
}

@DevicePreviews
@Composable
private fun MeScreenPreview() {
    AppPreview {
        MeScreen(
            onWallpaperClick = {},
            onSettingsClick = {},
            onStatusDarkModeSet = {},
            windowSizeClass = previewWindowSizeClass(),
            uiState = MeUiState(),
        )
    }
}
