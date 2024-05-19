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

package com.reach.modernandroid.feature.uilearn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.reach.base.ui.common.clickableNoVisualEffect
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.core.ui.common.widget.AppTopBarWithBack
import com.reach.modernandroid.core.ui.design.theme.AppTheme
import org.koin.compose.koinInject

fun NavGraphBuilder.lottieRoute() {
    composable<AppRoute.Lottie> {
        LottieRoute()
    }
}

@Composable
private fun LottieRoute(appUiState: AppUiState = koinInject()) {
    LottieScreen(onBackClick = { appUiState.getNavController().navigateUp() })
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun LottieScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBarWithBack(
            title = { Text(text = stringResource(id = R.string.uilearn_lottie_example)) },
            onBackClick = onBackClick,
        )

        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(state = rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            BasicUsage()
            TiktokLike()
        }
    }
}

@Composable
private fun BasicUsage() {
    LottieCard {
        Text(text = "Basic usage")

        Row(verticalAlignment = Alignment.CenterVertically) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.heart))
            LottieAnimation(composition = composition)

            Text(text = "Repeat once")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.heart))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )

            Text(text = "Repeat forever")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.heart))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                clipSpec = LottieClipSpec.Progress(0.5f, 0.75f),
            )

            Text(text = "Repeat forever from 50% to 75%")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val compositionResult =
                rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.heart))
            when {
                compositionResult.isLoading -> {
                    Text("Animation is loading...")
                }

                compositionResult.isFailure -> {
                    Text("Animation failed to load")
                }

                compositionResult.isSuccess -> {
                    LottieAnimation(
                        composition = compositionResult.value,
                        iterations = LottieConstants.IterateForever,
                    )
                }
            }

            Text(text = "Using LottieAnimationResult")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.heart))
            LottieAnimation(
                composition = composition,
                progress = { 0.65f },
            )

            Text(text = "Using LottieComposition")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.heart))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
            )

            Text(text = "Splitting out the animation driver")
        }
    }
}

@Composable
private fun TiktokLike() {
    LottieCard {
        Text(text = "Tiktok like")

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.heart))
        var isPlaying by remember { mutableStateOf(false) }
        var isLike by remember { mutableStateOf(false) }

        if (isLike) {
            val progress by animateLottieCompositionAsState(
                composition = composition,
            )
            LottieAnimation(
                composition = composition,
                progress = {
                    if (progress > 0.99f) {
                        isPlaying = false
                    }
                    progress
                },
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clickableNoVisualEffect {
                        if (isPlaying) {
                            return@clickableNoVisualEffect
                        }
                        isLike = false
                    },
            )
        } else {
            LottieAnimation(
                composition = composition,
                progress = { 0f },
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clickableNoVisualEffect {
                        isPlaying = true
                        isLike = true
                    },
            )
        }
    }
}

@Composable
private fun LottieCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.width(320.dp),
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
        ) {
            content()
        }
    }
}

@Preview(name = "mobile")
@Preview(name = "tablet", widthDp = 800)
@Composable
private fun LottieScreenPreview() {
    AppTheme {
        LottieScreen(onBackClick = {})
    }
}
