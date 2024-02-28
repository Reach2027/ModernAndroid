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

package com.reach.modernandroid.core.ui.design.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = LightColor.Primary,
    onPrimary = LightColor.OnPrimary,
    primaryContainer = LightColor.PrimaryContainer,
    onPrimaryContainer = LightColor.OnPrimaryContainer,
    secondary = LightColor.Secondary,
    onSecondary = LightColor.OnSecondary,
    secondaryContainer = LightColor.SecondaryContainer,
    onSecondaryContainer = LightColor.OnSecondaryContainer,
    tertiary = LightColor.Tertiary,
    onTertiary = LightColor.OnTertiary,
    tertiaryContainer = LightColor.TertiaryContainer,
    onTertiaryContainer = LightColor.OnTertiaryContainer,
    error = LightColor.Error,
    errorContainer = LightColor.ErrorContainer,
    onError = LightColor.OnError,
    onErrorContainer = LightColor.OnErrorContainer,
    background = LightColor.Background,
    onBackground = LightColor.OnBackground,
    surface = LightColor.Surface,
    onSurface = LightColor.OnSurface,
    surfaceVariant = LightColor.SurfaceVariant,
    onSurfaceVariant = LightColor.OnSurfaceVariant,
    outline = LightColor.Outline,
    inverseOnSurface = LightColor.InverseOnSurface,
    inverseSurface = LightColor.InverseSurface,
    inversePrimary = LightColor.InversePrimary,
    surfaceTint = LightColor.SurfaceTint,
    outlineVariant = LightColor.OutlineVariant,
    scrim = LightColor.Scrim,
)

private val DarkColors = darkColorScheme(
    primary = DarkColor.Primary,
    onPrimary = DarkColor.OnPrimary,
    primaryContainer = DarkColor.PrimaryContainer,
    onPrimaryContainer = DarkColor.OnPrimaryContainer,
    secondary = DarkColor.Secondary,
    onSecondary = DarkColor.OnSecondary,
    secondaryContainer = DarkColor.SecondaryContainer,
    onSecondaryContainer = DarkColor.OnSecondaryContainer,
    tertiary = DarkColor.Tertiary,
    onTertiary = DarkColor.OnTertiary,
    tertiaryContainer = DarkColor.TertiaryContainer,
    onTertiaryContainer = DarkColor.OnTertiaryContainer,
    error = DarkColor.Error,
    errorContainer = DarkColor.ErrorContainer,
    onError = DarkColor.OnError,
    onErrorContainer = DarkColor.OnErrorContainer,
    background = DarkColor.Background,
    onBackground = DarkColor.OnBackground,
    surface = DarkColor.Surface,
    onSurface = DarkColor.OnSurface,
    surfaceVariant = DarkColor.SurfaceVariant,
    onSurfaceVariant = DarkColor.OnSurfaceVariant,
    outline = DarkColor.Outline,
    inverseOnSurface = DarkColor.InverseOnSurface,
    inverseSurface = DarkColor.InverseSurface,
    inversePrimary = DarkColor.InversePrimary,
    surfaceTint = DarkColor.SurfaceTint,
    outlineVariant = DarkColor.OutlineVariant,
    scrim = DarkColor.Scrim,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (dynamicTheme && supportDynamicTheme()) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) DarkColors else LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = appShapes,
        typography = appTypography,
        content = content,
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportDynamicTheme() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
