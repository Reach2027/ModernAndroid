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

package com.reach.modernandroid.ui.feature.me

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.reach.modernandroid.ui.base.common.animation.topDestEnterTransition
import com.reach.modernandroid.ui.base.common.animation.topDestExitTransition
import com.reach.modernandroid.ui.base.common.navigation.AppRoute
import com.reach.modernandroid.ui.base.resource.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

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
internal fun MeRoute(
    viewModel: MeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MeScreen(uiState = uiState)
}

@Composable
private fun MeScreen(
    uiState: MeUiState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        PersonInfo(uiState = uiState)
        Spacer(modifier = Modifier.height(16.dp))
        DeviceInfo(uiState = uiState)
    }
}

@Composable
private fun PersonInfo(
    uiState: MeUiState,
) {
    Box {
        AsyncImage(
            model = uiState.imageUrl,
            contentDescription = "",
            modifier = Modifier
                .widthIn(max = 480.dp)
                .aspectRatio(16f / 9f),
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

@Preview(widthDp = 390)
@Composable
private fun MeScreenPreview() {
    AppTheme {
        MeScreen(MeUiState())
    }
}
