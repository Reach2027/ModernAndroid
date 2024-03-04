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

package com.reach.modernandroid.core.ui.common.permission

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.reach.base.android.common.util.toAppDetailSettings
import com.reach.modernandroid.core.ui.common.R
import com.reach.modernandroid.core.ui.design.animation.groupEnter
import com.reach.modernandroid.core.ui.design.animation.groupExit

private val ContentPadding = 32.dp

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionScreen(
    permission: String,
    @StringRes requestTitle: Int,
    onBackClick: () -> Unit,
    permissionGrantedContent: @Composable () -> Unit,
) {
    val permissionState = rememberPermissionState(permission = permission)
    if (permissionState.status.isGranted.not()) {
        LaunchedEffect(true) {
            permissionState.launchPermissionRequest()
        }
    }

    val context = LocalContext.current
    AnimatedVisibility(
        visible = permissionState.status.isGranted.not(),
        enter = fadeIn(animationSpec = groupEnter()),
        exit = fadeOut(animationSpec = groupExit()),
    ) {
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
                            Text(text = stringResource(id = R.string.later))
                        }
                        Spacer(modifier = Modifier.width(ContentPadding))
                        Button(onClick = { context.toAppDetailSettings() }) {
                            Text(text = stringResource(id = R.string.to_request_permission))
                        }
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = permissionState.status.isGranted,
        enter = fadeIn(animationSpec = groupEnter()),
        exit = fadeOut(animationSpec = groupExit()),
    ) {
        permissionGrantedContent()
    }
}
