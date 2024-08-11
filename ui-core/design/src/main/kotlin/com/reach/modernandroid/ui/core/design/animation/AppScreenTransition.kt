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

package com.reach.modernandroid.ui.core.design.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset

@Stable
fun topDestEnterTransition(): EnterTransition = fadeIn(animationSpec = groupEnter())

@Stable
fun topDestExitTransition(): ExitTransition = fadeOut(animationSpec = groupExit())

@Stable
fun enterScreenTransition(): EnterTransition = slideInHorizontally(
    animationSpec = groupEnter(visibilityThreshold = IntOffset.VisibilityThreshold),
    initialOffsetX = { it },
)

@Stable
fun exitScreenTransition(): ExitTransition = slideOutHorizontally(
    animationSpec = groupExit(visibilityThreshold = IntOffset.VisibilityThreshold),
    targetOffsetX = { -it / 4 },
) + fadeOut(animationSpec = groupExit())

@Stable
fun popEnterScreenTransition(): EnterTransition = slideInHorizontally(
    animationSpec = groupEnter(visibilityThreshold = IntOffset.VisibilityThreshold),
    initialOffsetX = { -it / 4 },
) + fadeIn(animationSpec = groupEnter())

@Stable
fun popExitScreenTransition(): ExitTransition = slideOutHorizontally(
    animationSpec = groupExit(visibilityThreshold = IntOffset.VisibilityThreshold),
    targetOffsetX = { it },
)
