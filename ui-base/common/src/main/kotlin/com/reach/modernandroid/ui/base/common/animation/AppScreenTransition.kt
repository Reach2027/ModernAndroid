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

package com.reach.modernandroid.ui.base.common.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset

private const val ENTER_STIFFNESS = Spring.StiffnessMediumLow
private const val EXIT_STIFFNESS = Spring.StiffnessMedium

fun topDestEnterTransition(): EnterTransition =
    fadeIn(animationSpec = spring(stiffness = ENTER_STIFFNESS))

fun topDestExitTransition(): ExitTransition =
    fadeOut(animationSpec = spring(stiffness = EXIT_STIFFNESS))

fun enterScreenTransition(): EnterTransition =
    fadeIn(animationSpec = spring(stiffness = ENTER_STIFFNESS)) + slideInHorizontally(
        animationSpec = spring(
            stiffness = ENTER_STIFFNESS,
            visibilityThreshold = IntOffset.VisibilityThreshold,
        ),
        initialOffsetX = { it },
    )

fun exitScreenTransition(): ExitTransition =
    fadeOut(animationSpec = spring(stiffness = EXIT_STIFFNESS))

fun popEnterScreenTransition(): EnterTransition =
    fadeIn(animationSpec = spring(stiffness = ENTER_STIFFNESS))

fun popExitScreenTransition(): ExitTransition =
    fadeOut(animationSpec = spring(stiffness = EXIT_STIFFNESS)) + slideOutHorizontally(
        animationSpec = spring(
            stiffness = EXIT_STIFFNESS,
            visibilityThreshold = IntOffset.VisibilityThreshold,
        ),
        targetOffsetX = { it },
    )
