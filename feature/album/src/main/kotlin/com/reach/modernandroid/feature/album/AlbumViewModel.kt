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

package com.reach.modernandroid.feature.album

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.reach.modernandroid.feature.data.album.LocalImageRepo
import com.reach.modernandroid.feature.data.album.model.LocalAlbumModel
import com.reach.modernandroid.feature.data.album.model.LocalImageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal data class AlbumUiState(
    val localAlbums: List<LocalAlbumModel> = emptyList(),
    val currentAlbum: LocalAlbumModel = LocalAlbumModel(
        albumId = "",
        albumName = "",
        coverId = "",
        coverUri = Uri.EMPTY,
        count = 0,
    ),
)

internal class AlbumViewModel(
    private val localImageRepo: LocalImageRepo,
) : ViewModel() {

    private val _previewIndex = MutableStateFlow(0)
    val previewIndex = _previewIndex.asStateFlow()

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState = _uiState.asStateFlow()

    var localImages: Flow<PagingData<LocalImageModel>> =
        localImageRepo.getLocalImages()
            .cachedIn(viewModelScope)

    fun setPreViewIndex(index: Int) {
        _previewIndex.value = index
    }

    fun changeAlbum(albumModel: LocalAlbumModel) {
        if (albumModel == _uiState.value.currentAlbum) {
            return
        }
        _uiState.value = _uiState.value.copy(currentAlbum = albumModel)
        localImages = localImageRepo.getLocalImages(albumModel.albumId.toLongOrNull())
            .cachedIn(viewModelScope)
    }

    fun getLocalAlbums() {
        if (_uiState.value.localAlbums.isNotEmpty()) {
            return
        }
        viewModelScope.launch {
            localImageRepo.getLocalAlbums().collect {
                _uiState.emit(_uiState.value.copy(localAlbums = it))
            }
        }
    }
}
