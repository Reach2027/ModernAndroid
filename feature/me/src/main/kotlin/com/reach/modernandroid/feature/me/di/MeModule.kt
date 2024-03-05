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

package com.reach.modernandroid.feature.me.di

import com.reach.base.android.common.di.deviceStateModule
import com.reach.modernandroid.feature.data.bingwallpaper.di.bingWallpaperDataModule
import com.reach.modernandroid.feature.me.MeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val meModule = module {
    includes(deviceStateModule, bingWallpaperDataModule)

    viewModelOf(::MeViewModel)
}
