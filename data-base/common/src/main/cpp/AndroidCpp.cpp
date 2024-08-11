#include <jni.h>

#include <string>
#include "CpuUtils.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_reach_base_common_jni_AndroidCpp_bindMainThread(JNIEnv *env, jobject thiz) {
    CpuUtils::bindMainThread();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_reach_base_common_jni_AndroidCpp_stringFromJni(JNIEnv *env,
                                                                jobject thiz) {
    std::string hello = "Hello Android Cpp";
    return env->NewStringUTF(hello.c_str());
}