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

package com.reach.modernandroid.feature.data.album.source

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.reach.modernandroid.feature.data.album.model.LocalImageModel

internal class LocalImagePagingSource(
    private val application: Application,
    private val albumId: Long? = null,
) : PagingSource<Int, LocalImageModel>() {

    companion object {
        internal const val PAGE_LIMIT = 500
        internal const val MAX_ITEM = PAGE_LIMIT * 3
    }

    override fun getRefreshKey(state: PagingState<Int, LocalImageModel>): Int? =
        state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocalImageModel> = try {
        val currentPage = params.key ?: 0
        val localImages = application.getLocalImage(
            albumId = albumId,
            offset = currentPage * PAGE_LIMIT,
            limit = PAGE_LIMIT,
        )
        LoadResult.Page(
            data = localImages,
            prevKey = if (currentPage < 1) null else currentPage - 1,
            nextKey = if (localImages.size < PAGE_LIMIT) null else currentPage + 1,
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
