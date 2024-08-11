package com.reach.base.common.jni

object AndroidCpp {

    init {
        System.loadLibrary("android_cpp")
    }

    external fun bindMainThread()

    external fun stringFromJni(): String
}