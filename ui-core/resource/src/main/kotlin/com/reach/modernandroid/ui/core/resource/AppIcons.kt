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

package com.reach.modernandroid.ui.core.resource

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

object AppIcons {
    val MeSelected = Icons.Rounded.Person
    val MeUnselected = Icons.Rounded.PersonOutline

    val MoreSelected = Icons.Rounded.RocketLaunch
    val MoreUnselected = Icons.Outlined.RocketLaunch

    val Back = Icons.AutoMirrored.Rounded.ArrowBackIos
    val MoreFunction = Icons.Rounded.MoreVert

    val Last = Icons.AutoMirrored.Rounded.ArrowBackIos
    val next = Icons.AutoMirrored.Rounded.ArrowForwardIos
}

@Preview
@Composable
private fun AppIconsPreview() {
    Column {
        Row(modifier = Modifier.padding(8.dp)) {
            Icon(imageVector = AppIcons.MeSelected, contentDescription = "")
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = AppIcons.MeUnselected, contentDescription = "")
        }

        Row(modifier = Modifier.padding(8.dp)) {
            Icon(imageVector = AppIcons.MoreSelected, contentDescription = "")
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = AppIcons.MoreUnselected, contentDescription = "")
        }

        Row(modifier = Modifier.padding(8.dp)) {
            Icon(imageVector = AppIcons.Back, contentDescription = "")
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = AppIcons.MoreFunction, contentDescription = "")
        }

        Row(modifier = Modifier.padding(8.dp)) {
            Icon(imageVector = AppIcons.Last, contentDescription = "")
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = AppIcons.next, contentDescription = "")
        }
    }
}
