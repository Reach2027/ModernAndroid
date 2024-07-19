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

package com.reach.modernandroid.feature.data.album

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.reach.modernandroid.feature.data.album.model.LocalAlbumModel
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import com.reach.modernandroid.feature.data.album.source.LocalImagePagingSource
import com.reach.modernandroid.feature.data.album.source.getLocalAlbums
import com.reach.modernandroid.feature.data.album.source.getLocalImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface LocalImageRepo {

    fun getLocalImages(albumId: Long? = null): Flow<PagingData<LocalImageModel>>

    fun getLocalImages(albumId: Long? = null, count: Int): Flow<List<LocalImageModel>>

    fun getLocalAlbums(): Flow<List<LocalAlbumModel>>
}

internal class DefaultLocalImageRepo(
    private val application: Application,
) : LocalImageRepo {

    override fun getLocalImages(albumId: Long?): Flow<PagingData<LocalImageModel>> =
        Pager(
            config = PagingConfig(
                pageSize = LocalImagePagingSource.PAGE_LIMIT,
                maxSize = LocalImagePagingSource.MAX_ITEM,
            ),
        ) {
            LocalImagePagingSource(application, albumId)
        }.flow

    override fun getLocalImages(albumId: Long?, count: Int): Flow<List<LocalImageModel>> = flow {
        emit(application.getLocalImage(albumId = albumId, limit = count))
    }

    override fun getLocalAlbums(): Flow<List<LocalAlbumModel>> = flow {
        emit(application.getLocalAlbums())
    }
}
