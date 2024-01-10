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

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import java.io.File
import java.io.FileWriter
import kotlin.test.Test

class ExampleTest {

    @Test
    fun testDisk() = runTest {
        val dir = File("/Volumes/ReachE/DiskTest")
        repeat(150) { cur ->
            val file = File(dir, "test$cur.txt")
            FileWriter(file).use { writer ->
                repeat(1000) {
                    writer.write("Line $it\n")
                }
                writer.flush()
            }
            delay(200L)
        }
    }

    @Test
    fun testStrategy() = runTest {
        val dir = File("/Volumes/ReachE/DiskTest")
        val arr = dir.listFiles() ?: return@runTest
        arr.sortBy { it.lastAccess() }
        arr[0]
//        val res = dir.cleanupDir(timeoutDay = 3, maxCount = -1, maxMegabytes = -0.5f)
    }

    @Test
    fun t1() = runTest {
        val stateTest = MutableStateFlow(1)

        launch {
            stateTest.collect {
                println(it)
            }
        }

        launch {
            delay(1000L)
            stateTest.value = 3
            stateTest.value = 4
        }

        launch {
            delay(1000L)
            stateTest.value = 2
        }
    }
}
