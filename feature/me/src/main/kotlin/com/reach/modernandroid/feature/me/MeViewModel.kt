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

package com.reach.modernandroid.feature.me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reach.base.common.Result
import com.reach.base.common.devicestate.DeviceState
import com.reach.base.common.devicestate.network.NetworkType
import com.reach.modernandroid.feature.data.bingwallpaper.BingWallpaperRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal data class MeUiState(
    val isImageLoading: Boolean = true,
    val imageUrl: String = "",
    val isNetworkAvailable: Boolean = false,
    val networkType: NetworkType = NetworkType.None,
)

internal class MeViewModel(
    private val deviceState: DeviceState,
    private val bingWallpaperRepo: BingWallpaperRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            setupCollector()
        }
    }

    private fun CoroutineScope.setupCollector() {
        launch {
            deviceState.isNetworkAvailable.collect {
                _uiState.emit(_uiState.value.copy(isNetworkAvailable = it))
            }
        }
        launch {
            deviceState.networkType.collect {
                _uiState.emit(_uiState.value.copy(networkType = it))
            }
        }

        launch {
            bingWallpaperRepo.getTodayWallpaper().collect {
                when (it) {
                    is Result.Success -> {
                        _uiState.emit(
                            _uiState.value.copy(
                                isImageLoading = false,
                                imageUrl = it.data.imageUrl,
                            ),
                        )
                    }

                    is Result.Error -> {}
                    is Result.Loading -> _uiState.emit(_uiState.value.copy(isImageLoading = true))
                }
            }
        }
    }
}
