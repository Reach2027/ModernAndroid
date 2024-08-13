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

package com.reach.modernandroid.data.core.datastore.di

import androidx.datastore.core.DataStore
import com.reach.base.common.di.QualifierCoroutineScope
import com.reach.base.common.di.coroutineScopeModule
import com.reach.modernandroid.core.data.datastore.SettingsPb
import com.reach.modernandroid.data.core.datastore.DefaultSettingsLocalSource
import com.reach.modernandroid.data.core.datastore.SettingsLocalSource
import com.reach.modernandroid.data.core.datastore.getSettingsDataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val dataStoreModule = module {
    includes(coroutineScopeModule)

    single<DataStore<SettingsPb>> {
        androidApplication().getSettingsDataStore()
    }

    factory<SettingsLocalSource> {
        DefaultSettingsLocalSource(
            get(),
            get(qualifier(QualifierCoroutineScope.AppIo)),
        )
    }
}
