#include <jni.h>

#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_reach_base_android_common_jni_AndroidCpp_stringFromJni(JNIEnv *env,
                                                                jobject thiz) {
    std::string hello = "Hello Android Cpp";
    return env->NewStringUTF(hello.c_str());
}