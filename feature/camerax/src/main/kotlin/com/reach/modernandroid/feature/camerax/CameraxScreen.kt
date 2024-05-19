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

package com.reach.modernandroid.feature.camerax

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.reach.base.ui.common.toDp
import com.reach.base.ui.common.widget.AsyncLocalImage
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.core.ui.common.navigation.screenComposable
import com.reach.modernandroid.core.ui.common.permission.RequestPermissionsScreen
import com.reach.modernandroid.core.ui.common.state.AppUiState
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

private const val RATIO_3_4 = 3f / 4f

fun NavGraphBuilder.cameraxRoute() {
    screenComposable<AppRoute.CameraX> {
        CameraxRoute()
    }
}

@SuppressLint("SourceLockedOrientationActivity")
@Composable
internal fun CameraxRoute(
    appUiState: AppUiState = koinInject(),
    viewModel: CameraxViewModel = koinNavViewModel(),
) {
    LifecycleStartEffect(true) {
        appUiState.setFullScreen(true)
        appUiState.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT)

        onStopOrDispose {
            appUiState.setFullScreen(false)
            appUiState.resetRequestedOrientation()
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RequestPermissionsScreen(
        permissions = listOf(android.Manifest.permission.CAMERA),
        requestTitle = R.string.camerax_request_permission,
        onBackClick = { appUiState.getNavController().navigateUp() },
    ) {
        CameraxScreen(
            navToAlbum = { appUiState.getNavController().navigate(AppRoute.Album) },
            takePhoto = { viewModel.takePhoto() },
            preview = viewModel.preview,
            imageCapture = viewModel.imageCapture,
            uiState = uiState,
        )
    }
}

@Composable
private fun CameraxScreen(
    navToAlbum: () -> Unit,
    takePhoto: () -> Unit,
    preview: Preview,
    imageCapture: ImageCapture,
    uiState: CameraUiState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Camera(
            preview = preview,
            imageCapture = imageCapture,
        )

        ActionBar(
            navToAlbum = navToAlbum,
            takePhoto = takePhoto,
            uri = uiState.uri,
        )
    }
}

@Composable
private fun Camera(
    preview: Preview,
    imageCapture: ImageCapture,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { context ->
            val view = PreviewView(context)
            view.implementationMode = PreviewView.ImplementationMode.COMPATIBLE

            preview.setSurfaceProvider(view.surfaceProvider)

            val cameraProvider = ProcessCameraProvider.getInstance(context).get()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture,
            )

            view
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(RATIO_3_4),
    )
}

@Composable
private fun BoxScope.ActionBar(
    navToAlbum: () -> Unit,
    takePhoto: () -> Unit,
    uri: Uri,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomStart)
            .padding(
                bottom = 64.dp + WindowInsets.navigationBars
                    .getBottom(LocalDensity.current)
                    .toDp(),
            ),
        contentAlignment = Alignment.Center,
    ) {
        AsyncLocalImage(
            model = uri,
            contentDescription = "",
            modifier = Modifier
                .padding(end = 240.dp)
                .size(56.dp, 56.dp)
                .clip(shape = CircleShape)
                .clickable { navToAlbum() },
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .size(64.dp, 64.dp)
                .clip(shape = CircleShape)
                .background(Color.White)
                .clickable { takePhoto() },
        )
    }
}
