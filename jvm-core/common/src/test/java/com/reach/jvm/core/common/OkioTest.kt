package com.reach.jvm.core.common

import com.reach.jvm.core.common.disk.DiskLruCache
import com.reach.jvm.core.common.disk.openDiskCache
import java.io.File
import kotlin.test.assertNull

class OkioTest {

    @org.junit.Test
    fun delete_test() {
        val cache: DiskLruCache = openDiskCache(
            File("R:\\TestCache"),
            maxCount = 5,
            timeoutDay = 100,
            fileType = "txt"
        )

//        cache.edit("3").apply {
//            newOutputStream(0).writer().use {
//                it.write("11111111111111")
//                it.flush()
//            }
//            commit()
//        }

//        cache.get("1").getFile(0)

        Thread.sleep(5000)
        assertNull(cache.get("2"))

        cache.close()
    }

}