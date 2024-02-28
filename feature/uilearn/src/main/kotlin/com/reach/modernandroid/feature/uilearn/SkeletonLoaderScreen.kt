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

package com.reach.modernandroid.ui.feature.uilearn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.reach.base.ui.common.widget.SkeletonLoader
import com.reach.modernandroid.core.ui.common.AppUiState
import com.reach.modernandroid.core.ui.common.navigation.AppRoute
import com.reach.modernandroid.core.ui.common.navigation.screenComposable
import com.reach.modernandroid.core.ui.common.widget.AppTopBarWithBack
import com.reach.modernandroid.core.ui.resource.theme.AppTheme
import com.reach.modernandroid.feature.uilearn.R
import org.koin.compose.koinInject

fun NavGraphBuilder.skeletonLoaderRoute() {
    screenComposable(AppRoute.SKELETON_LOADER) {
        SkeletonLoaderRoute()
    }
}

@Composable
internal fun SkeletonLoaderRoute(appUiState: AppUiState = koinInject()) {
    SkeletonLoaderScreen(
        onBackClick = { appUiState.getNavController().navigateUp() },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun SkeletonLoaderScreen(
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AppTopBarWithBack(
            title = { Text(text = stringResource(id = R.string.skeleton_loader)) },
            onBackClick = onBackClick,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            ),
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
            CustomLoader()
        }
    }
}

@Composable
private fun CustomLoader() {
    Column {
        SkeletonLoader(
            modifier = Modifier
                .width(320.dp)
                .height(320.dp)
                .clip(MaterialTheme.shapes.large),
        )
        Spacer(modifier = Modifier.height(16.dp))
        SkeletonLoader(
            modifier = Modifier
                .width(320.dp)
                .height(32.dp)
                .clip(MaterialTheme.shapes.small),
        )
    }
}

@Preview
@Composable
private fun SkeletonLoaderScreenPreview() {
    AppTheme {
        SkeletonLoaderScreen(
            onBackClick = {},
        )
    }
}
