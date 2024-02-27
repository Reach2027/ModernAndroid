package com.reach.modernandroid.data.base.datastore.di

import com.reach.modernandroid.data.base.datastore.DefaultUserSettingsRepository
import com.reach.modernandroid.data.base.datastore.UserSettingsRepository
import com.reach.modernandroid.data.base.datastore.userSettingsDataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataStoreModule = module {
    single<UserSettingsRepository> {
        DefaultUserSettingsRepository(androidApplication().userSettingsDataStore)
    }
}