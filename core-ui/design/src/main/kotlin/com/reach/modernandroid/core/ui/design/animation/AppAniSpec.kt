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

package com.reach.modernandroid.core.ui.design.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Stable

object AppAniSpec {

    const val STIFFNESS_WIDGET_ENTER = 800f
    const val STIFFNESS_WIDGET_EXIT = 1200f
    const val STIFFNESS_WIDGET_EXIT_LOW = 800f

    const val STIFFNESS_GROUP_ENTER = 400f
    const val STIFFNESS_GROUP_EXIT = 600f
    const val STIFFNESS_GROUP_EXIT_LOW = 400f

    const val DAMPING_RATIO = 0.6f
}

@Stable
fun <T> widgetEnter(
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    stiffness: Float = AppAniSpec.STIFFNESS_WIDGET_ENTER,
    visibilityThreshold: T? = null,
): SpringSpec<T> = spring(
    dampingRatio = dampingRatio,
    stiffness = stiffness,
    visibilityThreshold = visibilityThreshold,
)

@Stable
fun <T> widgetExit(
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    stiffness: Float = AppAniSpec.STIFFNESS_WIDGET_EXIT,
    visibilityThreshold: T? = null,
): SpringSpec<T> = spring(
    dampingRatio = dampingRatio,
    stiffness = stiffness,
    visibilityThreshold = visibilityThreshold,
)

@Stable
fun <T> groupEnter(
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    stiffness: Float = AppAniSpec.STIFFNESS_GROUP_ENTER,
    visibilityThreshold: T? = null,
): SpringSpec<T> = spring(
    dampingRatio = dampingRatio,
    stiffness = stiffness,
    visibilityThreshold = visibilityThreshold,
)

@Stable
fun <T> groupExit(
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    stiffness: Float = AppAniSpec.STIFFNESS_GROUP_EXIT,
    visibilityThreshold: T? = null,
): SpringSpec<T> = spring(
    dampingRatio = dampingRatio,
    stiffness = stiffness,
    visibilityThreshold = visibilityThreshold,
)
