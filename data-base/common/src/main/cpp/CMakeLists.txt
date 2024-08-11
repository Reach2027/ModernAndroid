cmake_minimum_required(VERSION 3.22.1)

project("android_cpp")

add_library(android_cpp
        SHARED
        AndroidCpp.cpp
        CpuUtils.h
        CpuUtils.cpp
)

# Include libraries needed for hello-jni lib
target_link_libraries(
        android_cpp
        android
        log
)