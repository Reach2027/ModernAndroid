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

package com.reach.modernandroid.di

import com.reach.modernandroid.MainActivityViewModel
import com.reach.modernandroid.core.ui.common.di.appUiStateStateModule
import com.reach.modernandroid.feature.album.di.albumModule
import com.reach.modernandroid.feature.bingwallpaper.di.bingWallpaperModule
import com.reach.modernandroid.feature.data.settings.di.settingsRepoModule
import com.reach.modernandroid.feature.me.di.meModule
import com.reach.modernandroid.feature.settings.di.settingsModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    includes(
        appUiStateStateModule,
        meModule,
        bingWallpaperModule,
        settingsModule,
        settingsRepoModule,
        albumModule,
    )

    viewModelOf(::MainActivityViewModel)
}
