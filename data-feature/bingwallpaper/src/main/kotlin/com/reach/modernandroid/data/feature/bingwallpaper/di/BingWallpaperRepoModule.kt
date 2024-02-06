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

package com.reach.modernandroid.data.feature.bingwallpaper.di

import com.reach.core.jvm.common.di.QualifierDispatchers
import com.reach.core.jvm.common.di.dispatcherModule
import com.reach.modernandroid.data.base.network.di.httpClientModule
import com.reach.modernandroid.data.feature.bingwallpaper.BingWallpaperRepository
import com.reach.modernandroid.data.feature.bingwallpaper.DefaultBingWallpaperRepo
import com.reach.modernandroid.data.feature.bingwallpaper.source.BingWallpaperApi
import com.reach.modernandroid.data.feature.bingwallpaper.source.BingWallpaperPagingSource
import com.reach.modernandroid.data.feature.bingwallpaper.source.DefaultBingWallpaperApi
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val bingWallpaperRepoModule = module {
    includes(httpClientModule, dispatcherModule)

    factoryOf(::DefaultBingWallpaperApi) {
        bind<BingWallpaperApi>()
    }

    factoryOf(::BingWallpaperPagingSource)

    factory<BingWallpaperRepository> {
        DefaultBingWallpaperRepo(
            get(),
            get(),
            get(qualifier(QualifierDispatchers.IO)),
        )
    }
}
