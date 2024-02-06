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

package com.reach.modernandroid.ui.feature.album

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import com.reach.modernandroid.ui.base.common.AppUiState
import com.reach.modernandroid.ui.base.common.navigation.AppRoute
import com.reach.modernandroid.ui.base.common.navigation.screenComposable
import com.reach.modernandroid.ui.base.common.widget.AppTopBarWithBack
import com.reach.modernandroid.ui.base.resource.theme.AppTheme
import org.koin.compose.koinInject

fun NavGraphBuilder.albumRoute() {
    screenComposable(
        route = AppRoute.ALBUM,
    ) {
        AlbumRoute()
    }
}

@Composable
private fun AlbumRoute(
    appUiState: AppUiState = koinInject(),
) {
    AlbumScreen(
        onBackClick = { appUiState.navController.navigateUp() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppTopBarWithBack(
            title = { Text(text = stringResource(id = R.string.photos)) },
            onBackClick = onBackClick,
        )
    }
}

@Preview
@Composable
private fun AlbumScreenPreview() {
    AppTheme {
        AlbumScreen(
            onBackClick = {},
        )
    }
}
