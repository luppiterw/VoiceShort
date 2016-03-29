#include <jni.h>
#include <android/log.h>

JNIEXPORT jint JNICALL
Java_TestNative_firstfunction(JNIEnv *env, jclass type) {

    // TODO
    __android_log_print(ANDROID_LOG_DEBUG, "Hughie", "Java_TestNative_firstfunction");

}