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

package com.reach.modernandroid.ui.feature.camerax

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.reach.modernandroid.ui.base.common.AppUiState
import com.reach.modernandroid.ui.base.common.navigation.AppRoute
import com.reach.modernandroid.ui.base.common.navigation.screenComposable
import org.koin.compose.koinInject

fun NavGraphBuilder.cameraxRoute() {
    screenComposable(
        route = AppRoute.CAMERAX,
    ) {
        CameraxRoute()
    }
}

@Composable
internal fun CameraxRoute() {
    val appUiState = koinInject<AppUiState>()

    CameraxScreen(
        navToAlbum = { appUiState.navController.navigate(AppRoute.ALBUM) },
    )
}

@Composable
private fun CameraxScreen(
    navToAlbum: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "CameraxRoute")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navToAlbum() }) {
            Text(text = "To Album")
        }
    }
}