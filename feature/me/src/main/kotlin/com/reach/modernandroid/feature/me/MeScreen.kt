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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.reach.base.ui.common.devicepreview.DevicePreviews
import com.reach.base.ui.common.devicepreview.previewWindowSizeClass
import com.reach.base.ui.common.toDp
import com.reach.base.ui.common.widget.SkeletonAsyncImage
import com.reach.base.ui.common.widget.SkeletonLoader
import com.reach.modernandroid.core.ui.common.AppPreview
import com.reach.modernandroid.core.ui.common.AppUiState
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.core.ui.design.AppIcons
import com.reach.modernandroid.core.ui.design.animation.topDestEnterTransition
import com.reach.modernandroid.core.ui.design.animation.topDestExitTransition
import com.reach.modernandroid.core.ui.design.theme.AppColor
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

fun NavGraphBuilder.meRoute() {
    composable(
        route = AppRoute.ME,
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
        windowSizeClass = appUiState.getWindowSizeClass(),
        uiState = uiState,
        onWallpaperClick = { appUiState.getNavController().navigate(AppRoute.BING_WALLPAPER) },
        onSettingsClick = { appUiState.getNavController().navigate(AppRoute.SETTINGS) },
    )
}

@Composable
private fun MeScreen(
    windowSizeClass: WindowSizeClass,
    uiState: MeUiState,
    onWallpaperClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
        MeScreenExpanded(
            uiState = uiState,
            onWallpaperClick = onWallpaperClick,
            onSettingsClick = onSettingsClick,
        )
    } else {
        MeScreenCompat(
            uiState = uiState,
            onWallpaperClick = onWallpaperClick,
            onSettingsClick = onSettingsClick,
        )
    }
}

@Composable
private fun MeScreenExpanded(
    uiState: MeUiState,
    onWallpaperClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val systemBarH = WindowInsets.systemBars.getTop(LocalDensity.current).toDp()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = systemBarH)
            .padding(horizontal = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.large),
        ) {
            PersonInfo(
                uiState = uiState,
                onWallpaperClick = onWallpaperClick,
                onSettingsClick = onSettingsClick,
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .weight(1f),
        ) {
            DeviceInfo(uiState = uiState)
        }
    }
}

@Composable
private fun MeScreenCompat(
    uiState: MeUiState,
    onWallpaperClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        PersonInfo(
            uiState = uiState,
            onWallpaperClick = onWallpaperClick,
            onSettingsClick = onSettingsClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        DeviceInfo(uiState = uiState)
    }
}

@Composable
private fun PersonInfo(
    uiState: MeUiState,
    onWallpaperClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Box {
        if (uiState.isImageLoading) {
            SkeletonLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
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
                    .aspectRatio(16f / 9f),
            )
        }
        Icon(
            imageVector = AppIcons.Settings,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clickable { onSettingsClick() },
            tint = AppColor.White,
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
                id = R.string.network_available,
                uiState.isNetworkAvailable.toString(),
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.network_type, uiState.networkType.name))
    }
}

@DevicePreviews
@Composable
private fun MeScreenPreview() {
    AppPreview {
        MeScreen(
            windowSizeClass = previewWindowSizeClass(),
            uiState = MeUiState(),
            onWallpaperClick = {},
            onSettingsClick = {},
        )
    }
}
