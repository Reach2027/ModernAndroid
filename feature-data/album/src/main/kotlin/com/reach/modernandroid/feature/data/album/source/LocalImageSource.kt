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
import android.os.SystemClock
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.reach.modernandroid.feature.data.album.model.LocalAlbumModel
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MILLISECOND = 1000L

internal suspend fun Application.getLocalAlbums(): List<LocalAlbumModel> =
    withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
        )

        SystemClock.uptimeMillis()

        val localAlbumModels = mutableListOf<LocalAlbumModel>()
        launch {
            val coverImage = async { getLocalImage(limit = 1) }.await()
            if (coverImage.isEmpty()) {
                return@launch
            }
            val allPhotosAlbum = LocalAlbumModel(
                albumId = "",
                albumName = "",
                coverId = coverImage[0].id,
                coverUri = coverImage[0].uri,
                count = async { getImageCount() }.await(),
            )
            localAlbumModels.add(0, allPhotosAlbum)
        }

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
                putString(
                    ContentResolver.QUERY_ARG_SQL_GROUP_BY,
                    MediaStore.Images.ImageColumns.BUCKET_ID,
                )
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
                "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC GROUP BY ${MediaStore.Images.ImageColumns.BUCKET_ID}",
            )
        }?.use {
            val bucketIdIndex =
                it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID)
            val bucketNameIndex =
                it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)

            while (it.moveToNext()) {
                val bucketId = it.getLong(bucketIdIndex)
                val bucketName = it.getStringOrNull(bucketNameIndex) ?: ""

                val coverImage = async { getLocalImage(albumId = bucketId, limit = 1)[0] }.await()
                val albumInfo = LocalAlbumModel(
                    albumId = bucketId.toString(),
                    albumName = bucketName,
                    coverId = coverImage.id,
                    coverUri = coverImage.uri,
                    count = async { getImageCount(bucketId) }.await(),
                )
                localAlbumModels.add(albumInfo)
            }
        }
        return@withContext localAlbumModels
    }

internal suspend fun Application.getImageCount(albumId: Long? = null): Int =
    withContext(Dispatchers.IO) {
        val projection = arrayOf(MediaStore.Images.ImageColumns._ID)

        val selection: String? = if (albumId == null) {
            null
        } else {
            "${MediaStore.Images.ImageColumns.BUCKET_ID} = ?"
        }
        val selectionArgs: Array<String>? = if (albumId == null) {
            null
        } else {
            arrayOf(albumId.toString())
        }

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null,
        )?.use { return@withContext it.count }

        return@withContext 0
    }

internal suspend fun Application.getLocalImage(
    albumId: Long? = null,
    offset: Int = 0,
    limit: Int? = null,
): List<LocalImageModel> = withContext(Dispatchers.IO) {
    val projection = arrayOf(
        MediaStore.Images.ImageColumns._ID,
        MediaStore.Images.ImageColumns.DATE_MODIFIED,
        MediaStore.Images.ImageColumns.BUCKET_ID,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
    )

    val selection: String? = if (albumId == null) {
        null
    } else {
        "${MediaStore.Images.ImageColumns.BUCKET_ID}=?"
    }
    val selectionArgs: Array<String>? = if (albumId == null) {
        null
    } else {
        arrayOf(albumId.toString())
    }

    val localImageModels = mutableListOf<LocalImageModel>()

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        val bundle = Bundle().apply {
            putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
            putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
            putStringArray(
                ContentResolver.QUERY_ARG_SORT_COLUMNS,
                arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED),
            )
            putInt(
                ContentResolver.QUERY_ARG_SORT_DIRECTION,
                ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
            )
            if (limit != null) {
                putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
            }
            putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
        }
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            bundle,
            null,
        )
    } else {
        var sortOrder = "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC OFFSET $offset"
        if (limit != null) {
            sortOrder += "LIMIT $limit"
        }
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder,
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
            val modifierTime = it.getLong(dateIndex) * MILLISECOND
            val bucketId = it.getLong(bucketIdIndex)
            val bucketName = it.getStringOrNull(bucketNameIndex) ?: ""

            localImageModels.add(
                LocalImageModel(
                    id = imageId,
                    uri = uri,
                    modifierTime = modifierTime,
                    albumId = bucketId.toString(),
                    albumName = bucketName,
                ),
            )
        }
    }
    return@withContext localImageModels
}
