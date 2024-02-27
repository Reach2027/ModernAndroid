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

package com.reach.modernandroid.data.base.datastore

import android.app.Application
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

internal class SettingsSerializer : Serializer<SettingsPb> {

    override val defaultValue: SettingsPb = SettingsPb.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingsPb =
        try {
            SettingsPb.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read user_settings.proto", exception)
        }

    override suspend fun writeTo(t: SettingsPb, output: OutputStream) {
        t.writeTo(output)
    }
}

internal fun Application.getSettingsDataStore(): DataStore<SettingsPb> =
    DataStoreFactory.create(
        serializer = SettingsSerializer(),
    ) {
        dataStoreFile("settings.pb")
    }
