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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.FileTime

private const val DAY_MILLIS = 1000L * 60L * 60L * 24L

private const val MEGABYTES = 1024L * 1024L

private const val LAST_ACCESS_TIME = "lastAccessTime"

/**
 * 清理缓存文件夹
 *
 * @param timeoutDay 保留最近多少天，单位 天
 * @param maxCount 最大文件个数
 * @param maxMegabytes 最大占用大小，单位 MB
 */
fun File.cleanupDir(
    timeoutDay: Int = -1,
    maxCount: Int = -1,
    maxMegabytes: Float = -1f,
) = CoroutineScope(Dispatchers.IO).launch {
    if (this@cleanupDir.isFile) {
        return@launch
    }
    runTimeoutStrategy(this@cleanupDir, timeoutDay)
    runMaxCountStrategy(this@cleanupDir, maxCount)
    runMaxSizeStrategy(this@cleanupDir, maxMegabytes)
    this.cancel()
}

/**
 * 文件夹清理策略——时间过期
 * @param dir 文件夹
 * @param timeoutDay 保留最近多少天，单位 天
 */
private fun runTimeoutStrategy(dir: File, timeoutDay: Int) {
    if (timeoutDay < 1) return
    val child = dir.listFiles() ?: return

    val timeoutTime = System.currentTimeMillis() - timeoutDay * DAY_MILLIS
    child.forEach {
        // 在过期时间之前
        if (it.lastAccess() < timeoutTime) {
            it.delete()
        }
    }
}

/**
 * 文件夹清理策略——固定文件个数
 * @param dir 文件夹
 * @param maxCount 最大文件个数
 */
private fun runMaxCountStrategy(dir: File, maxCount: Int) {
    if (maxCount < 1) return
    val child = dir.listFiles() ?: return

    val currentCount = child.size
    if (currentCount <= maxCount) {
        return
    }
    child.sortBy { it.lastAccess() }
    val iterator = child.iterator()
    var count = currentCount - maxCount
    while (count > 0 && iterator.hasNext()) {
        iterator.next().delete()
        count--
    }
}

/**
 * 文件夹清理策略——固定占用大小
 * @param dir 文件夹
 * @param maxMegabytes 最大占用大小，单位 MB
 */
private fun runMaxSizeStrategy(dir: File, maxMegabytes: Float) {
    if (maxMegabytes <= 0f) return
    val child = dir.listFiles() ?: return

    var currentSize = 0L
    child.sortBy { it.lastAccess() }
    val fileSizeList = ArrayList<Long>(child.size)
    child.forEach {
        val size = it.bytes()
        fileSizeList.add(size)
        currentSize += size
    }
    val maxSize = (maxMegabytes * MEGABYTES).toLong()
    val iterator = child.iterator()
    var currentIndex = 0
    while (currentSize > maxSize && iterator.hasNext()) {
        iterator.next().delete()
        currentSize -= fileSizeList[currentIndex++]
    }
}

/**
 * @return 文件最近访问时间，单位 ms
 */
fun File.lastAccess(): Long {
    val map: Map<String, Any>? = Files.readAttributes(this.toPath(), LAST_ACCESS_TIME)
    val time: FileTime? = map?.get(LAST_ACCESS_TIME) as? FileTime
    return time?.toMillis() ?: this.lastModified()
}

/**
 * @return 文件占用 byte
 */
fun File.bytes(): Long {
    if (this.isFile) {
        return this.length()
    }
    var size = 0L
    this.listFiles()?.forEach {
        size += it.bytes()
    }
    return size
}
