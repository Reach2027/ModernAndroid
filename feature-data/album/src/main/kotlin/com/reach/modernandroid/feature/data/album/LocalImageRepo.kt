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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import com.reach.modernandroid.feature.data.album.source.LocalImagePagingSource
import com.reach.modernandroid.feature.data.album.source.MAX_ITEM
import com.reach.modernandroid.feature.data.album.source.PAGE_LIMIT
import kotlinx.coroutines.flow.Flow

interface LocalImageRepo {

    fun getLocalImages(): Flow<PagingData<LocalImageModel>>
}

internal class DefaultLocalImageRepo(
    private val localImagePagingSource: LocalImagePagingSource,
) : LocalImageRepo {

    override fun getLocalImages(): Flow<PagingData<LocalImageModel>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_LIMIT,
                maxSize = MAX_ITEM,
            ),
        ) {
            localImagePagingSource
        }.flow
}
