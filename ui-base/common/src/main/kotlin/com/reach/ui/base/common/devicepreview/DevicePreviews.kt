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

package com.reach.ui.base.common.devicepreview

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Preview(name = "phone-port", device = "spec:width=378dp,height=840dp,dpi=420")
@Preview(name = "phone-land", device = "spec:width=840dp,height=378dp,dpi=420")
@Preview(name = "tablet-port", device = "spec:width=720dp,height=1152dp,dpi=420")
@Preview(name = "tablet-land", device = "spec:width=1152dp,height=720dp,dpi=420")
annotation class DevicePreviews

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun previewWindowSizeClass(): WindowSizeClass = WindowSizeClass.calculateFromSize(
    DpSize(
        LocalConfiguration.current.screenWidthDp.dp,
        LocalConfiguration.current.screenHeightDp.dp,
    ),
)
