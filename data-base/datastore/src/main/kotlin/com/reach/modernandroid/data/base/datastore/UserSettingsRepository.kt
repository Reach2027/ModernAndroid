package com.reach.modernandroid.data.base.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {

    val userSettings: Flow<UserSettings>

    suspend fun setDynamicTheme(dynamicTheme: Boolean)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

}

internal class DefaultUserSettingsRepository(private val dataStore: DataStore<UserSettings>) :
    UserSettingsRepository {

    override val userSettings = dataStore.data

    override suspend fun setDynamicTheme(dynamicTheme: Boolean) {
        dataStore.updateData {
            it.toBuilder()
                .setDynamicTheme(dynamicTheme)
                .build()
        }
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataStore.updateData {
            it.toBuilder()
                .setDarkThemeConfig(darkThemeConfig)
                .build()
        }
    }
}