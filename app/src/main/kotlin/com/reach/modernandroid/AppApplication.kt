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

import android.app.Application
import com.reach.modernandroid.ui.base.common.di.appUiStateStateModule
import com.reach.modernandroid.ui.feature.bingwallpaper.di.bingWallpaperModule
import com.reach.modernandroid.ui.feature.me.di.meModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@AppApplication)
            modules(
                appUiStateStateModule,
                meModule,
                bingWallpaperModule,
            )
        }
    }
}
