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

package com.reach.modernandroid.feature.data.bingwallpaper

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.reach.base.jvm.common.Result
import com.reach.base.jvm.common.flowResult
import com.reach.modernandroid.feature.data.bingwallpaper.model.BingWallpaperModel
import com.reach.modernandroid.feature.data.bingwallpaper.source.BingWallpaperApi
import com.reach.modernandroid.feature.data.bingwallpaper.source.BingWallpaperPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface BingWallpaperRepository {

    fun getTodayWallpaper(): Flow<Result<BingWallpaperModel>>

    fun bingWallpaperFlow(): Flow<PagingData<BingWallpaperModel>>
}

internal class DefaultBingWallpaperRepo(
    private val bingWallpaperApi: BingWallpaperApi,
    private val bingWallpaperPagingSource: BingWallpaperPagingSource,
    private val dispatcher: CoroutineDispatcher,
) : BingWallpaperRepository {

    override fun getTodayWallpaper(): Flow<Result<BingWallpaperModel>> =
        flowResult(dispatcher) {
            bingWallpaperApi.getBingWallpaper(0, 1)
                .images[0]
        }

    override fun bingWallpaperFlow(): Flow<PagingData<BingWallpaperModel>> =
        Pager(config = PagingConfig(pageSize = 8)) {
            bingWallpaperPagingSource
        }.flow
}
