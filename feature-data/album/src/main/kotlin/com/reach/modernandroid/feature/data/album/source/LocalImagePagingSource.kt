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
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.reach.modernandroid.feature.data.album.model.LocalImageModel

internal const val LIMIT = 500

internal class LocalImagePagingSource(
    private val application: Application,
) : PagingSource<Int, LocalImageModel>() {

    override fun getRefreshKey(state: PagingState<Int, LocalImageModel>): Int? =
        state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocalImageModel> = try {
        val currentPage = params.key ?: 0
        val localImages = queryLocalImage(currentPage * LIMIT)
        LoadResult.Page(
            data = localImages,
            prevKey = if (currentPage < 1) null else currentPage - 1,
            nextKey = if (localImages.size < LIMIT) null else currentPage + 1,
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    private fun queryLocalImage(offset: Int): List<LocalImageModel> {
        val contentResolver = application.contentResolver

        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATE_MODIFIED,
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
        )

        val localImageModels = mutableListOf<LocalImageModel>()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val bundle = Bundle().apply {
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED),
                )
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
                )
                putInt(ContentResolver.QUERY_ARG_LIMIT, LIMIT)
                putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
            }
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                bundle,
                null,
            )
        } else {
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC LIMIT $LIMIT OFFSET $offset",
            )
        }?.use {
            val idIndex = it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
            val dateIndex =
                it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED)
            val bucketIdIndex =
                it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID)
            val bucketNameIndex =
                it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)

            while (it.moveToNext()) {
                val imageId = it.getLong(idIndex).toString()
                val uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId,
                )
                val modifierTime = it.getLong(dateIndex) * 1000L
                val bucketId = it.getLong(bucketIdIndex)
                val bucketName = it.getStringOrNull(bucketNameIndex) ?: ""

                localImageModels.add(
                    LocalImageModel(
                        id = imageId,
                        uri = uri,
                        modifierTime = modifierTime,
                        albumId = bucketId,
                        albumName = bucketName,
                    ),
                )
            }
        }
        return localImageModels
    }
}
