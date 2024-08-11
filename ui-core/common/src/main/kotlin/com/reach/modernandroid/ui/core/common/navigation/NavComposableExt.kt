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

package com.reach.modernandroid.ui.core.common.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.reach.modernandroid.ui.core.design.animation.enterScreenTransition
import com.reach.modernandroid.ui.core.design.animation.exitScreenTransition
import com.reach.modernandroid.ui.core.design.animation.popEnterScreenTransition
import com.reach.modernandroid.ui.core.design.animation.popExitScreenTransition
import kotlin.reflect.KType

inline fun <reified T : Any> NavGraphBuilder.screenComposable(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline enterTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    @JvmSuppressWildcards EnterTransition
    ) = { enterScreenTransition() },
    noinline exitTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    @JvmSuppressWildcards ExitTransition
    ) = { exitScreenTransition() },
    noinline popEnterTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    @JvmSuppressWildcards EnterTransition
    ) = { popEnterScreenTransition() },
    noinline popExitTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    @JvmSuppressWildcards ExitTransition
    ) = { popExitScreenTransition() },
    noinline sizeTransform: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() ->
        @JvmSuppressWildcards SizeTransform?
    )? = null,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    this.composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        sizeTransform = sizeTransform,
        content = content,
    )
}
