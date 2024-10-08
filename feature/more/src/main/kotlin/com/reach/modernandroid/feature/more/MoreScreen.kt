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

package com.reach.modernandroid.feature.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.reach.modernandroid.ui.core.common.navigation.AppRoute
import com.reach.modernandroid.ui.core.common.state.AppUiState
import com.reach.modernandroid.ui.core.design.animation.topDestEnterTransition
import com.reach.modernandroid.ui.core.design.animation.topDestExitTransition
import com.reach.modernandroid.ui.core.design.theme.AppTheme
import org.koin.compose.koinInject

fun NavGraphBuilder.moreRoute() {
    composable<AppRoute.More>(
        enterTransition = { topDestEnterTransition() },
        exitTransition = { topDestExitTransition() },
    ) {
        MoreRoute()
    }
}

@Composable
internal fun MoreRoute() {
    val appUiState = koinInject<AppUiState>()

    val functions: List<Function> = Function.entries

    MoreScreen(
        functions = functions,
        navController = appUiState.getNavController(),
    )
}

@Composable
private fun MoreScreen(
    functions: List<Function>,
    navController: NavController,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item(
                key = "Top Padding",
                span = { GridItemSpan(maxLineSpan) },
                contentType = "Top Padding",
            ) {
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(
                items = functions,
                key = { it.text },
                span = {
                    if (it.isTitle) {
                        GridItemSpan(maxLineSpan)
                    } else {
                        GridItemSpan(1)
                    }
                },
                contentType = { it.isTitle },
            ) {
                MoreFunctionItem(
                    function = it,
                    navController = navController,
                )
            }
        }
    }
}

@Composable
private fun MoreFunctionItem(
    function: Function,
    navController: NavController,
) {
    if (function.isTitle) {
        Text(
            text = function.text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp),
        )
    } else {
        Button(
            onClick = { function.navTo(navController) },
            modifier = Modifier.wrapContentSize(),
        ) {
            Text(text = function.text)
        }
    }
}

@Preview(widthDp = 390)
@Composable
private fun MoreScreenPreview() {
    AppTheme {
        MoreScreen(
            functions = Function.entries,
            navController = NavController(LocalContext.current),
        )
    }
}
