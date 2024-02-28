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

package com.reach.modernandroid.data.feature.bingwallpaper.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.reach.modernandroid.data.feature.bingwallpaper.model.BingWallpaperModel

internal class BingWallpaperPagingSource(
    private val bingWallpaperApi: BingWallpaperApi,
) : PagingSource<Int, BingWallpaperModel>() {
    override fun getRefreshKey(state: PagingState<Int, BingWallpaperModel>): Int? =
        state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BingWallpaperModel> = try {
        val nextBeforeDays = params.key ?: 0
        val bingWallpapersModel = bingWallpaperApi.getBingWallpaper(
            beforeDays = nextBeforeDays,
            count = 8,
        )
        LoadResult.Page(
            data = bingWallpapersModel.images,
            prevKey = null,
            nextKey = nextBeforeDays + 7,
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
