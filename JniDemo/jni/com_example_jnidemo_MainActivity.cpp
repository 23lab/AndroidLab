#include "com_example_jnidemo_MainActivity.h"
#include "jni.h"
#include "android/log.h"

#define LOGD(fmt, ...)   __android_log_print(ANDROID_LOG_INFO, "TAG", fmt, __VA_ARGS__)

/*
 * Class:     com_example_jnidemo_MainActivity
 * Method:    localRef500
 * Signature: ()V
 */JNIEXPORT void JNICALL Java_com_example_jnidemo_MainActivity_localRef500(
        JNIEnv * env, jclass) {
    for (int i = 0; i < 500; i++) {
        LOGD("Java_com_example_jnidemo_MainActivity_localRef500 %d", i);
        jclass clz = env->FindClass("java/lang/String");
        //		env->DeleteLocalRef(clz);
    }

}

/*
 * Class:     com_example_jnidemo_MainActivity
 * Method:    localRef512
 * Signature: ()V
 */JNIEXPORT void JNICALL Java_com_example_jnidemo_MainActivity_localRef512(
        JNIEnv * env, jclass) {
    for (int i = 0; i < 512; i++) {
        LOGD("Java_com_example_jnidemo_MainActivity_localRef512 %d", i);
        jclass clz = env->FindClass("java/lang/String");
        //      env->DeleteLocalRef(clz);
    }
}

/*
 * Class:     com_example_jnidemo_MainActivity
 * Method:    localRef256
 * Signature: ()V
 */JNIEXPORT void JNICALL Java_com_example_jnidemo_MainActivity_localRef256(
        JNIEnv * env, jclass) {
    for (int i = 0; i < 256; i++) {
        LOGD("Java_com_example_jnidemo_MainActivity_localRef256 %d", i);
        jclass clz = env->FindClass("java/lang/String");
        //      env->DeleteLocalRef(clz);
    }
}
