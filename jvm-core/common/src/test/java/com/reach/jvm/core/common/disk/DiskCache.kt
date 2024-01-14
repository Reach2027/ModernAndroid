package com.reach.jvm.core.common.disk

import java.io.File

/**
 * 使用示例
 * ```kt
 *         // 创建
 *         val cache: DiskLruCache = openDiskCache(
 *             File(""),
 *             maxCount = 5,
 *             fileType = "txt"
 *         )
 *
 *         // 存放文件
 *         cache.edit("1").apply {
 *             newOutputStream(0).writer().use {
 *                 it.write("11111111111111")
 *                 it.flush()
 *             }
 *             commit()
 *         }
 *
 *         // 获取文件
 *         cache.get("1")?.apply {
 *             getFile(0)
 *         }
 *
 *         // 关闭
 *         cache.close()
 * ```
 *
 * @param maxSize 缓存最大空间占用，byte，<= 0 在不开启此策略
 * @param maxCount 缓存最大文件个数 <= 0 在不开启此策略
 * @param timeoutDay 过期天数，<= 0 在不开启此策略
 * @param valueCount 每个 key 对应的文件个数
 * @param fileType 文件类型，如 jpg、png
 */
fun openDiskCache(
    dir: File,
    maxSize: Long = -1,
    maxCount: Int = -1,
    timeoutDay: Int = -1,
    valueCount: Int = 1,
    fileType: String = "",
): DiskLruCache {
    return DiskLruCache.open(
        dir,
        1,
        maxSize,
        maxCount,
        timeoutDay,
        valueCount,
        fileType,
    )
}