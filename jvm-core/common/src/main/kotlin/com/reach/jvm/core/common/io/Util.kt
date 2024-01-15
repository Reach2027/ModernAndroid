package com.reach.jvm.core.common.io

import java.io.Closeable

fun Closeable.closeQuietly() {
    try {
        close()
    } catch (e: RuntimeException) {
        throw e
    } catch (_: Exception) {}
}