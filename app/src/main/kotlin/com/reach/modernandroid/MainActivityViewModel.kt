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

package com.reach.modernandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reach.modernandroid.data.core.datastore.model.UserSettings
import com.reach.modernandroid.feature.data.settings.SettingsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainActivityUiState(
    val isLoading: Boolean = true,
    val settings: UserSettings = UserSettings(),
)

internal class MainActivityViewModel(
    settingsRepo: SettingsRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepo.userSettings.collect {
                _uiState.emit(
                    _uiState.value.copy(
                        isLoading = it.isLoading,
                        settings = it,
                    ),
                )
            }
        }
    }
}
