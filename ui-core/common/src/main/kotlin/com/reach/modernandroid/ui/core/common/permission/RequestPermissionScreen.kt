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

package com.reach.modernandroid.ui.core.common.permission

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.reach.base.common.util.toAppDetailSettings
import com.reach.modernandroid.ui.core.design.animation.groupEnter
import com.reach.modernandroid.ui.core.design.animation.groupExit
import com.reach.modernandroid.ui.core.design.R as designR

private val ContentPadding = 32.dp

@Composable
fun RequestPermissionsScreen(
    permissions: List<String>,
    @StringRes requestTitle: Int,
    onBackClick: () -> Unit,
    grantedCallback: () -> Unit = {},
    permissionGrantedContent: @Composable () -> Unit,
) {
    if (permissions.size > 1) {
        RequestMultiplePermissionsScreen(
            permissions = permissions,
            requestTitle = requestTitle,
            onBackClick = onBackClick,
            grantedCallback = grantedCallback,
            permissionGrantedContent = permissionGrantedContent,
        )
    } else {
        RequestPermissionScreen(
            permission = permissions[0],
            requestTitle = requestTitle,
            onBackClick = onBackClick,
            grantedCallback = grantedCallback,
            permissionGrantedContent = permissionGrantedContent,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionScreen(
    permission: String,
    @StringRes requestTitle: Int,
    onBackClick: () -> Unit,
    grantedCallback: () -> Unit = {},
    permissionGrantedContent: @Composable () -> Unit,
) {
    val permissionState = rememberPermissionState(permission = permission)
    if (permissionState.status.isGranted.not()) {
        LaunchedEffect(true) {
            permissionState.launchPermissionRequest()
        }
    }
    LaunchedEffect(permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            grantedCallback()
        }
    }

    RequestPermissionContent(
        isGranted = permissionState.status.isGranted,
        requestTitle = requestTitle,
        onBackClick = onBackClick,
        permissionGrantedContent = permissionGrantedContent,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestMultiplePermissionsScreen(
    permissions: List<String>,
    @StringRes requestTitle: Int,
    onBackClick: () -> Unit,
    grantedCallback: () -> Unit = {},
    permissionGrantedContent: @Composable () -> Unit,
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = permissions)
    if (permissionsState.allPermissionsGranted.not()) {
        LaunchedEffect(true) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            grantedCallback()
        }
    }

    RequestPermissionContent(
        isGranted = permissionsState.allPermissionsGranted,
        requestTitle = requestTitle,
        onBackClick = onBackClick,
        permissionGrantedContent = permissionGrantedContent,
    )
}

@Composable
private fun RequestPermissionContent(
    isGranted: Boolean,
    @StringRes requestTitle: Int,
    onBackClick: () -> Unit,
    permissionGrantedContent: @Composable () -> Unit,
) {
    AnimatedContent(
        targetState = isGranted,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            fadeIn(animationSpec = groupEnter()) + slideInHorizontally(
                animationSpec = groupEnter(visibilityThreshold = IntOffset.VisibilityThreshold),
                initialOffsetX = { it },
            ) togetherWith fadeOut(animationSpec = groupExit()) + slideOutHorizontally(
                animationSpec = groupExit(visibilityThreshold = IntOffset.VisibilityThreshold),
                targetOffsetX = { -it },
            )
        },
        label = "",
    ) { granted ->
        if (granted) {
            permissionGrantedContent()
        } else {
            RequestPermissionHint(
                requestTitle = requestTitle,
                onBackClick = onBackClick,
            )
        }
    }
}

@Composable
private fun RequestPermissionHint(
    @StringRes requestTitle: Int,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(ContentPadding),
            ) {
                Text(
                    text = stringResource(id = requestTitle),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(ContentPadding))
                Row {
                    Button(onClick = onBackClick) {
                        Text(text = stringResource(id = designR.string.later))
                    }
                    Spacer(modifier = Modifier.width(ContentPadding))
                    Button(onClick = { context.toAppDetailSettings() }) {
                        Text(text = stringResource(id = designR.string.to_request_permission))
                    }
                }
            }
        }
    }
}
