package com.reach.base.android.common.jni

object AndroidCpp {

    init {
        System.loadLibrary("android_cpp")
    }

    external fun stringFromJni(): String
}