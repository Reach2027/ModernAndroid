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

package com.reach.base.ui.common.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.DefaultModelEqualityDelegate
import coil.compose.EqualityDelegate

@Composable
fun AsyncLocalImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    showBg: Boolean = true,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    clipToBounds: Boolean = true,
    modelEqualityDelegate: EqualityDelegate = DefaultModelEqualityDelegate,
) {
    var isSuccess by remember { mutableStateOf(false) }

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = if (showBg && isSuccess.not()) {
            modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                    ),
                ),
                shape = RectangleShape,
            )
        } else {
            modifier
        },
        transform = transform,
        onState = { state ->
            isSuccess = state is AsyncImagePainter.State.Success
            if (onState != null) {
                onState(state)
            }
        },
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        clipToBounds = clipToBounds,
        modelEqualityDelegate = modelEqualityDelegate,
    )
}

@Composable
fun SkeletonAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeHolderModifier: Modifier,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    clipToBounds: Boolean = true,
    modelEqualityDelegate: EqualityDelegate = DefaultModelEqualityDelegate,
) {
    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    Box {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            modifier = if (isSuccess) modifier else placeHolderModifier,
            transform = transform,
            onState = { state ->
                isLoading = state is AsyncImagePainter.State.Loading
                isSuccess = state is AsyncImagePainter.State.Success
                isError = state is AsyncImagePainter.State.Error
                if (onState != null) {
                    onState(state)
                }
            },
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
            clipToBounds = clipToBounds,
            modelEqualityDelegate = modelEqualityDelegate,
        )

        LoadingState(isLoading = isLoading, modifier = placeHolderModifier)

        ErrorState(isError = isError, modifier = placeHolderModifier)
    }
}

@Composable
private fun LoadingState(
    isLoading: Boolean,
    modifier: Modifier,
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        SkeletonLoader(modifier = modifier)
    }
}

@Composable
private fun ErrorState(
    isError: Boolean,
    modifier: Modifier,
) {
    AnimatedVisibility(
        visible = isError,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "The image failed to load",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
