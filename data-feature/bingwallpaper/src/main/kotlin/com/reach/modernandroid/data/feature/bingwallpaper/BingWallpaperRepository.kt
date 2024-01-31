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

package com.reach.modernandroid.data.feature.bingwallpaper

import com.reach.core.jvm.common.Result
import com.reach.core.jvm.common.httpResult
import com.reach.modernandroid.data.feature.bingwallpaper.model.BingWallpaperModel
import com.reach.modernandroid.data.feature.bingwallpaper.model.BingWallpapersModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import java.util.Locale

const val BING_BASE_URL = "https://www.bing.com"
private const val BING_IMAGE_URL = "/HPImageArchive.aspx"

interface BingWallpaperRepository {

    fun getTodayWallpaper(): Flow<Result<BingWallpaperModel>>
}

internal class DefaultBingWallpaperRepo(
    private val httpClient: HttpClient,
) : BingWallpaperRepository {

    override fun getTodayWallpaper(): Flow<Result<BingWallpaperModel>> = httpResult {
        getBingWallpaper(1).images[0]
    }

    private suspend fun getBingWallpaper(count: Int): BingWallpapersModel {
        check(count > 0) { "Image count must be > 0" }
        check(count < 9) { "Image count must be < 9" }

        return httpClient.get(BING_BASE_URL + BING_IMAGE_URL) {
            url {
                parameters.apply {
                    append("format", "js")
                    append("uhd", "1")
                    append("idx", "0")
                    append("n", count.toString())
                    append("mkt", Locale.getDefault().toLanguageTag())
                }
            }
        }.body()
    }
}
