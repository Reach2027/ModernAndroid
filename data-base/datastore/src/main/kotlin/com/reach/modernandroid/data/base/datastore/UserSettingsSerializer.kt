package com.reach.modernandroid.data.base.datastore

import android.app.Application
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

internal class UserSettingsSerializer : Serializer<UserSettings> {

    override val defaultValue: UserSettings = UserSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSettings =
        try {
            UserSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read user_settings.proto", exception)
        }


    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Application.userSettingsDataStore: DataStore<UserSettings> by dataStore(
    fileName = "user_settings.pb",
    serializer = UserSettingsSerializer(),
)