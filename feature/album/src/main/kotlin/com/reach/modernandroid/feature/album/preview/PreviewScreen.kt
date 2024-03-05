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

package com.reach.modernandroid.feature.album.preview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.reach.base.ui.common.widget.AsyncLocalImage
import com.reach.modernandroid.core.ui.common.state.AppUiState
import com.reach.modernandroid.core.ui.common.widget.AppTopBarWithBack
import com.reach.modernandroid.feature.album.AlbumViewModel
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import kotlinx.coroutines.flow.Flow

@Composable
internal fun PreviewRoute(
    viewModel: AlbumViewModel,
    appUiState: AppUiState,
) {
    val index by viewModel.previewIndex.collectAsStateWithLifecycle()

    PreviewScreen(
        onBackClick = { appUiState.getNavController().navigateUp() },
        onIndexChange = { viewModel.setPreViewIndex(it) },
        localImages = viewModel.localImages,
        index = index,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun PreviewScreen(
    onBackClick: () -> Unit,
    onIndexChange: (Int) -> Unit,
    localImages: Flow<PagingData<LocalImageModel>>,
    index: Int,
) {
    val items: LazyPagingItems<LocalImageModel> = localImages.collectAsLazyPagingItems()

    val pagerState = rememberPagerState(initialPage = index) {
        items.itemCount
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            onIndexChange(it)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        HorizontalPager(state = pagerState) {
            PreviewItem(localImageModel = items[it])
        }

        Box(modifier = Modifier.height(IntrinsicSize.Max)) {
            AppTopBarWithBack(
                title = { },
                onBackClick = onBackClick,
            )
        }
    }
}

@Composable
private fun PreviewItem(localImageModel: LocalImageModel?) {
    if (localImageModel == null) {
        return
    }
    AsyncLocalImage(
        model = localImageModel.uri,
        contentDescription = "",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
    )
}
